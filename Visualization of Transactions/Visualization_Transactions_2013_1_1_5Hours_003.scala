//spark-shell --packages com.databricks:spark-csv_2.10:1.1.0,graphframes:graphframes:0.1.0-spark1.6
//==============================================================
// Creates the javascript load lines for the visualization
// Bit coin graph from  2013/1/1 0:0 (epoch 1356998400) to    04:59:59 1357016399 - 5 hours
//
//
import scala.util.{Try, Success, Failure}
//==============================================================
val RDDVout = sc.textFile("/data/bitcoin/vout.csv").map(line => line.split(",")).filter( x => x.length == 4 && Try(x(1).toFloat).isSuccess)

//Ouputs
val RDDTx = sc.textFile("/data/bitcoin/transactions.csv").map(line => line.split(",")).filter( x => x.length == 5 && Try(x(2).toInt).isSuccess).map( x=> ( x(0), x(2).toInt )).filter{case(tx,epoch)=> epoch > 1356998400 && epoch < 1357016399  } //2013/1/1 0:0 1356998400   04:59:59 1357016399

val RDDVoutCurrent = RDDVout.map( x=> ( x(0) , (x(1).toFloat, x(3) ) ))
val RDDVoutCurrentAddresses = RDDTx.join(RDDVoutCurrent).map{ case(tx,(timevalue,(amount,pubkey))) => (tx,(timevalue,amount,pubkey))}

//Inputs
val RDDVin = sc.textFile("/data/bitcoin/vin.csv").map(line => line.split(",")).filter( x => x.length == 3).map( x=> ( x(0), (x(1), x(2) ) )).join(RDDTx).map{case( tx,((preTx,index),epoch)) =>  ( preTx+"_"+index, tx )} //Filter inputs to epoch cut off

val RDDVoutPrev = RDDVout.map( x=> ( x(0)+"_"+x(2), (x(1).toFloat, x(3) ) ))
val RDDVoutPrevAddresses = RDDVin.join(RDDVoutPrev).map{ case(prev_index,( tx,(amount, pubkey))) => (tx,( 0,amount,pubkey))}


///////////////// CREATE VISUALIZATION DATA //////////////////////////////////////
//   [  (tx,( IN=0, amount,pubkey))  +  (tx,(timevalue,amount,pubkey))
//addTransaction3(1002, [ [0,10.0,40],[12345, 8.5, 14],[12345, 0.5, 15],[12345, 1.5, 16]]);
val RDDCompleteTxforViz = RDDVoutPrevAddresses.union(RDDVoutCurrentAddresses).mapValues{ case(a,b,c) => "["+ a +","+ b +",'"+ c +"']"}.reduceByKey( (x, y) => x +","+ y).map{case(tx,nodelist)=> "addTransaction('" + tx + "',[" + nodelist + "]);" }

RDDCompleteTxforViz.coalesce(1,true).saveAsTextFile("/user/group-aj/VISUALIZATION_2013_1_1")  

