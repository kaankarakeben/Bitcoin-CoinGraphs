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
val vin_df = sqlContext.load("com.databricks.spark.csv", Map("path" -> vinFile, "header" -> "true"))
val vout_df = sqlContext.load("com.databricks.spark.csv", Map("path" -> voutFile, "header" -> "true"))

tx_df.registerTempTable("tx")
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

// Selecting pubkeys from final data to construct the vertices of graphs
val wallets = sqlContext.sql("select distinct src as pubkey from transactions union all select distinct dst as pubkey from transactions")
wallets.registerTempTable("wallets")
val vertices = sqlContext.sql("select distinct pubkey as id from wallets")

// Writing the vertices table to HDFS for backup
vertices.write.parquet("/user/group-aj/vertices")
