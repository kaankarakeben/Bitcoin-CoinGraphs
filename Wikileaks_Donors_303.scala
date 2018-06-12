//spark-shell --packages com.databricks:spark-csv_2.10:1.1.0,graphframes:graphframes:0.1.0-spark1.6
//==============================================================
import scala.util.{Try, Success, Failure}
//==============================================================
// Donatetion to Wikileaks bitcoin address
//        {1HB5XMLmzFVj8ALj6mfBsbifRoD4miY36v}   source:https://shop.wikileaks.org/donate
//==============================================================

//Inputs from Donations
val RDDVin = sc.textFile("/data/bitcoin/vin.csv").map(line => line.split(",")).filter( x => x.length == 3).map( x=> ( x(1)+"_"+x(2), x(0) ) )

val RDDVout = sc.textFile("/data/bitcoin/vout.csv").map(line => line.split(",")).filter( x => x.length == 4 && Try(x(1).toFloat).isSuccess)

val RDDVoutPrev = RDDVout.map( x=> ( x(0)+"_"+x(2), (x(1).toFloat, x(3) ) ))
val RDDVoutPrevAddresses = RDDVin.join(RDDVoutPrev).map{ case(prev_index,( tx,(inamount, pubkey))) => (tx,(inamount,pubkey))}


//Ouputs to Wikileaks
val RDDTx = sc.textFile("/data/bitcoin/transactions.csv").map(line => line.split(",")).filter( x => x.length == 5 && Try(x(2).toInt).isSuccess).map( x=> ( x(0), x(2).toInt ))

val RDDVoutCurrent = RDDVout.filter{ x => x(3)=="{1HB5XMLmzFVj8ALj6mfBsbifRoD4miY36v}" }.map( x=> ( x(0) , (x(1).toFloat, x(3) ) ))
val RDDVoutCurrentAddresses = RDDTx.join(RDDVoutCurrent).map{ case(tx,(timevalue,(amountToWiki,pubkey))) => (tx,(amountToWiki,timevalue))}

//Combine
val RDDWikiDonors = RDDVoutPrevAddresses.join(RDDVoutCurrentAddresses).map{ case (tx,( (inamount,pubkey) ,(amountToWiki,timevalue))) => (tx, (inamount,pubkey, amountToWiki )) }
//Totals
val RDDWikiDonorTotals = RDDWikiDonors.reduceByKey((x,y)=> (x._1 + y._1, "-", 0 ))

//////////////////////////////////////////////////////////////////////////////////////////////////
//Top 30 Donations - by amount - Allocate Donation = prorate amount gone to WIKI address.
val RDDWikiDonorAllocations = RDDWikiDonors.join(RDDWikiDonorTotals).map{ case(tx, ( (inamount,pubkey, amountToWiki ), (totalInAmount, p1, p2))) => (pubkey, (inamount * amountToWiki/totalInAmount,1))}.reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2)).map(item => item.swap).sortByKey(true, 1).top(30).map(item => item.swap)

RDDWikiDonorAllocations.take(30).foreach(println)
//------------------------------------------------------------------------------------------------
pubkey  |   total donation ( pro-rated )  | number of payments
({1LNWw6yCxkUmkhArb2Nf2MPw6vG7u5WG7q},(96.189926,7))    ----> https://en.wikipedia.org/wiki/Mt._Gox   [Mt. Gox's "green address" wallet]
({1BiZSHyPuVLiPNV7GTg5okStXKm1FTNSb7},(74.3,1))
({1FZVyaDzeQD2N85Ea6kbcW2LW3FdUxrfdP},(63.33333,1))
({1MvwZ6e2PA9QxNimjvq5VuzXpicW2rbzf3},(55.355766,5))
({17SC6Ps71YMtexXjejdcV7HFJDYXKYDrKY},(50.0,1))
({19TCgtx62HQmaaGy8WNhLvoLXLr7LvaDYn},(49.98,1))
({1GJx75qFbevZpeJ2giYbnrBjXbpnDdQZUq},(46.453026,1))
({17zeTMh8xXeXXjZnbULXV3g3t3f7pftnEh},(45.0,1))
({13vFf3MZKxSA3Q9e14c8xUXbMpHQn1wCgq},(45.0,1))
({1C2UNnYzEzmJ5qpyXDrcA3k6MLru6yiJF3},(42.052334,1))
({12fLFY2cMoUstdQESwu7U8qNP9BpDHWwUC},(41.00286,1))
({1Cj24RN6apbAV36ErYbEM7xKud11ozTVHS},(39.054363,1))
({1Huk5VnjvJY6dVyGb3w72wMrqSNZvga1Nz},(32.274723,1))
({19y1ENhaiswqpT7dwZgDciBEicQJNGu41d},(32.237198,1))
({1GYMagx3YrWr9C8a2itabnw7zftP7suCtW},(32.07089,1))
({1jLofSmwEBz7Vmo9R7mNcjMcgBZKvi1Ss},(31.65611,1))
({1CP4bsMWuqCfVFgU7FPTS2Jmi5jrA8SA9W},(30.036095,1))
({1Pp6VksaHVwEzHM43CLk4KXwLQdtAXQdPP},(29.085001,1))
({1K7Cc6wANHnfsXMT43LBqEn7tMVeW38uqX},(28.98223,1))
({18pcznb96bbVE1mR7Di3hK7oWKsA1fDqhJ},(28.812,1))
({1NLzUGGsFt68Y4qZ4KFmbdP7mKueZfqocq},(27.616112,1))
({1Foa8SZCn9cmXJUf4sa3GnhxfkAHYSbZaG},(25.0,2))
({18GYce4UbZgQa8HLm9pACtVCWkM1iRAnxN},(25.0,1))
({168FmNwh5wZ6MQgp93WpxiNcMkvzWUmvCB},(24.98,1))
({1DRSNwA1Nw2Mw4DdYU966muEYQ145XEoEH},(24.28,1))
({1LcKW8givtCqZeQXb7T1APy3TjoW4BMiKj},(24.131893,1))
({142JZ7CkMVkinTBe4dXeHs5J865b3vZPre},(23.58,1))
({12oxboyGq2q2bx5JwtMU9JYvWgXEx8LWF8},(23.23,1))
({161SkPXMWuMvekXVY329i7sBNJ9oyTPuDr},(21.659128,1))
({17Mad42WnwrTs52buhjhSuQj71BwHjcz4x},(20.675669,1))

///////////////////////////////////////////////////////////////////////////////////////////////
//Total amount paid to Wikileaks
val RDDAmountsPaid = RDDVoutCurrentAddresses.map{ case(tx,(amountToWiki,timevalue))=> (amountToWiki)}.reduce( (x,y) => (x + y))
//---------------------------------------------------------------------------------------------
RDDAmountsPaid: Float = 3885.7659

///////////////////////////////////////////////////////////////////////////////////////////////
//Amounts paid by Date
import java.text.SimpleDateFormat
val RDDAmountsPaidbyDate = RDDVoutCurrentAddresses.map{ case(tx,(amountToWiki,timevalue))=> ( new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date (timevalue*1000L)),amountToWiki)}.reduceByKey((x,y)=> (x + y))
RDDAmountsPaidbyDate.coalesce(1,true).saveAsTextFile("/user/group-aj/Wikileaks_Payments_Dated")
//hadoop fs -cat /user/group-aj/Wikileaks_Payments_Dated/part-00000


