spark-shell --packages com.databricks:spark-csv_2.10:1.1.0,graphframes:graphframes:0.1.0-spark1.6

import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.util.IntParam
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

//reading in the separate files
val txFile = "/data/bitcoin/transactions.csv"
val vinFile = "/data/bitcoin/vin.csv"
val voutFile = "/data/bitcoin/vout.csv"
val sqlContext = new SQLContext(sc)
val tx_df = sqlContext.load("com.databricks.spark.csv", Map("path" -> txFile, "header" -> "true"))
val tx_df1 = tx_df.filter(tx_df("time") > 1381017600).filter(tx_df("time") < 1383696000) // 3 month data slice - 06/08/2013 00:00 - 06.11.2013 00:00
val vin_df = sqlContext.load("com.databricks.spark.csv", Map("path" -> vinFile, "header" -> "true"))
val vout_df = sqlContext.load("com.databricks.spark.csv", Map("path" -> voutFile, "header" -> "true"))

tx_df1.registerTempTable("tx")
vin_df.registerTempTable("vin")
vout_df.registerTempTable("vout")

// First table: joining transactions and vout
val table1 = sqlContext.sql("select a.tx_hash, a.time, b.pubkey, b.value from tx a, vout b where a.tx_hash=b.hash")
table1.registerTempTable("table1")

// Second table: joining first table with vin
val table2 = sqlContext.sql("select a.tx_hash, a.time, a.pubkey, a.value, b.tx_hash as prev_tx, b.vout from table1 a, vin b where a.tx_hash=b.txid")
table2.registerTempTable("table2")

// Last table: Getting the previous transcation id
val transactions = sqlContext.sql("select a.pubkey as dst, b.pubkey as src, CAST(a.value AS decimal(36,4)) value, CAST(a.time AS INT) time, a.tx_hash as tx_id from table2 a, vout b where a.prev_tx=b.hash and a.vout=b.n and a.pubkey<>b.pubkey order by a.time asc")
transactions.registerTempTable("transactions")

// Writing the final table to HDFS for backup
transactions.write.parquet("/user/group-aj/tx")

transactions.registerTempTable("edges")

// Creating three versions of the same final table transactions table
val ab = sqlContext.sql(" select tx_id as ab_tx_id, src as a, dst as b, time as ab_time, value as ab_value from edges")
ab.registerTempTable("ab")

val bc1 =sqlContext.sql("select a as b1, b as c1, ab_value as bc1_value, ab_time as bc1_time, ab_tx_id as bc1_tx_id from ab")
bc1.registerTempTable("bc1")

val ac2 =sqlContext.sql("select a as a1, b as c2, ab_value as ac2_value, ab_time as ac2_time, ab_tx_id as ac2_tx_id from ab")

// First neighbours
val abc = ab.join(bc1, ab.col("b") === bc1.col("b1")).distinct
// Filtering by time and selecting columns
val abc1 = abc.filter(abc("ab_time") < abc("bc1_time")).select("a", "b","c1", "bc1_time", "ab_tx_id", "bc1_tx_id")

// Second neighbours
val abc1c2 = abc1.join(ac2, abc1.col("c1") === ac2.col("a1")).distinct
// Filtering by time and selecting columns
val abc1c2_ = abc1c2.filter(abc1c2("bc1_time") < abc1c2("ac2_time")).select("a", "b","c1","c2","ab_tx_id", "bc1_tx_id","ac2_tx_id")
abc1c2_.write.parquet("dailytest")

// Third neighbours
val vertexTriangle = abc1c2_.filter("a=c2").select("a", "b","c1","ab_tx_id","bc1_tx_id","ac2_tx_id").distinct
