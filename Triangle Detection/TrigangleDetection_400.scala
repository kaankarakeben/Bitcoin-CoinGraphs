//spark-shell --packages com.databricks:spark-csv_2.10:1.1.0,graphframes:graphframes:0.1.0-spark1.6
//==============================================================
//import org.apache.spark.HashPartitioner
import scala.util.{Try, Success, Failure}

//==============================================================
val RDDVout = sc.textFile("/data/bitcoin/vout.csv").map(line => line.split(",")).filter( x => x.length == 4 && Try(x(1).toFloat).isSuccess)

//Inputs
val RDDVin = sc.textFile("/data/bitcoin/vin.csv").map(line => line.split(",")).filter( x => x.length == 3).map( x=> ( x(1)+"_"+x(2), x(0) ) )

val RDDVoutPrev = RDDVout.map( x=> ( x(0)+"_"+x(2), (x(1).toFloat, x(3) ) ))
val RDDVoutPrevAddresses = RDDVin.join(RDDVoutPrev).map{ case(prev_index,( tx,(amount, pubkey))) => (tx,( "'IN'",amount,pubkey))}

//Ouputs
val RDDTx = sc.textFile("/data/bitcoin/transactions.csv").map(line => line.split(",")).filter( x => x.length == 5 && Try(x(2).toInt).isSuccess).map( x=> ( x(0), x(2).toInt ))

val RDDVoutCurrent = RDDVout.map( x=> ( x(0) , (x(1).toFloat, x(3) ) ))
val RDDVoutCurrentAddresses = RDDTx.join(RDDVoutCurrent).map{ case(tx,(timevalue,(amount,pubkey))) => (tx,(timevalue,amount,pubkey))}


///////////////// CREATE VISUALIZATION DATA //////////////////////////////////////
//   [  (tx,( IN, amount,pubkey))  +  (tx,(timevalue,amount,pubkey))
//addTransaction3(1002, [ ['IN',10.0,40],[12345, 8.5, 14],[12345, 0.5, 15],[12345, 1.5, 16]]);
//val RDDCompleteTxforViz = RDDVoutPrevAddresses.union(RDDVoutCurrentAddresses).mapValues{ case(a,b,c) => "["+ a +","+ b +",'"+ c +"']"}.reduceByKey( (x, y) => x +","+ y).map{case(tx,nodelist)=> "addTransaction('" + tx + "',[" + nodelist + "]);" }
/////////RDDCompleteTxforViz.take(10).foreach(println)   //RDDCompleteTxforViz.count():53879702
/////////RDDCompleteTxforViz.saveAsTextFile("/user/group-aj/RDDCompleteTxforViz.csv")

////////////////  TRIANGLE DETECTION /////////////////////////////////////////////

//Neighbours = 1 : Edge list [x, y, -amount, time]
val RDDNeighbours_1 = RDDVoutPrevAddresses.join(RDDVoutCurrentAddresses).map{ case (tx,( (inTag, xamount, xpubkey) ,(timevalue,yamount,ypubkey))) => (xpubkey, (ypubkey,timevalue )) }

//1		(w1,(w2,100))
//RDDNeighbours_1.take(10).foreach(println) 

//2    (w2,((w4,101),(w1,100)))
var RDDN = RDDNeighbours_1.map{ case(x,(y,time)) => ( y, (x, time)) }
//RDDN.take(10).foreach(println)
var RDDNeighbours_2 = RDDNeighbours_1.join(RDDN).filter{ case(common,((y,yt),(x,xt))) => yt > xt }.map{ case(common,((y,yt),(x,xt))) => (y,(x,xt,common,yt)) }


//3:   (w4,((w1,120),(w1,100,w2,101)))
var RDDNeighbours_3 = RDDNeighbours_1.join(RDDNeighbours_2).filter{ case(common,((z,zt),(x,xt,y,yt))) => (zt > yt)&&(z==x) }.map{ case(common,((z,zt),(x,xt,y,yt))) => (x,y,common, xt,yt, zt) }
RDDNeighbours_3.coalesce(1,true).saveAsTextFile("/user/group-aj/Triangles")
