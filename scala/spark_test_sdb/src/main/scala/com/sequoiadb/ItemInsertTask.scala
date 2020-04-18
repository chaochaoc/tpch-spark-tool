package com.sequoiadb

import com.sequoiadb.pojo._
import com.sequoiadb.utils.Tools
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SparkSession}

object ItemInsertTask {

  def main(spark: SparkSession, itemName: String): Unit = {

    val sc: SparkContext = spark.sparkContext

    val base: String = s"${Tools.basePath}/gen/tbl"

    import spark.implicits._
    itemName match {
      case "customer" => {
        val customerStrRDD: RDD[String] = sc.textFile(s"$base/customer.tbl")
        val customerRDD: RDD[Customer] = customerStrRDD.map(line => {
          val rows: Array[String] = line.split("[|]")
          Customer(rows(0).toInt, rows(1), rows(2), rows(3).toInt, rows(4), rows(5).toFloat, rows(6), rows(7))
        })

        val customerSet: Dataset[Customer] = customerRDD.toDS()
        customerSet.createOrReplaceTempView("customer_tmp")

        spark.sql("insert into table customer select * from customer_tmp")
      }
      case "lineitem" => {
        val lineitemStrRDD: RDD[String] = sc.textFile(s"$base/lineitem.tbl")
        val lineitemRDD: RDD[Lineitem] = lineitemStrRDD.map(line => {
          val rows: Array[String] = line.split("[|]")
          Lineitem(rows(0).toInt, rows(1).toInt, rows(2).toInt, rows(3).toInt, rows(4).toFloat, rows(5).toFloat, rows(6).toFloat, rows(7).toFloat, rows(8), rows(9), rows(10), rows(11), rows(12), rows(13), rows(14), rows(15))
        })

        val lineitemDS: Dataset[Lineitem] = lineitemRDD.toDS()
        lineitemDS.createOrReplaceTempView("lineitem_tmp")

        spark.sql("insert into table lineitem select * from lineitem_tmp")
      }
      case "orders" => {
        val ordersStrRDD: RDD[String] = sc.textFile(s"$base/orders.tbl")
        val ordersRDD: RDD[Orders] = ordersStrRDD.map(line => {
          val rows: Array[String] = line.split("[|]")
          Orders(rows(0).toInt, rows(1).toInt, rows(2), rows(3).toFloat, rows(4), rows(5), rows(6), rows(7).toInt, rows(8))
        })

        val ordersDS: Dataset[Orders] = ordersRDD.toDS()
        ordersDS.createOrReplaceTempView("orders_tmp")

        spark.sql("insert into table orders select * from orders_tmp")
      }
      case "part" => {
        val partStrRDD: RDD[String] = sc.textFile(s"$base/part.tbl")
        val partRDD: RDD[Part] = partStrRDD.map(line => {
          val rows: Array[String] = line.split("[|]")
          Part(rows(0).toInt, rows(1), rows(2), rows(3), rows(4), rows(5).toInt, rows(6), rows(7).toFloat, rows(8))
        })

        val partDS: Dataset[Part] = partRDD.toDS()
        partDS.createOrReplaceTempView("part_tmp")

        spark.sql("insert into table part select * from part_tmp")
      }
      case "partsupp" => {
        val partsuppStrRDD: RDD[String] = sc.textFile(s"$base/partsupp.tbl")
        val partsuppRDD: RDD[Partsupp] = partsuppStrRDD.map(line => {
          val rows: Array[String] = line.split("[|]")
          Partsupp(rows(0).toInt, rows(1).toInt, rows(2).toInt, rows(3).toFloat, rows(4))
        })

        val partsuppDS: Dataset[Partsupp] = partsuppRDD.toDS()
        partsuppDS.createOrReplaceTempView("partsupp_tmp")

        spark.sql("insert into table partsupp select * from partsupp_tmp")
      }
      case "region" => {
        val regionStrRDD: RDD[String] = sc.textFile(s"$base/region.tbl")
        val regionRDD: RDD[Region] = regionStrRDD.map(line => {
          val rows: Array[String] = line.split("[|]")
          Region(rows(0).toInt, rows(1), rows(2))
        })

        val regionDS: Dataset[Region] = regionRDD.toDS()
        regionDS.createOrReplaceTempView("region_tmp")

        spark.sql("insert into table region select * from region_tmp")
      }
      case "supplier" => {
        val supplierStrRDD: RDD[String] = sc.textFile(s"$base/supplier.tbl")
        val supplierRDD: RDD[Supplier] = supplierStrRDD.map(line => {
          val rows: Array[String] = line.split("[|]")
          Supplier(rows(0).toInt, rows(1), rows(2), rows(3).toInt, rows(4), rows(5), rows(6))
        })

        val supplierDS: Dataset[Supplier] = supplierRDD.toDS()
        supplierDS.createOrReplaceTempView("supplier_tmp")

        spark.sql("insert into table supplier select * from supplier_tmp")
      }
      case "nation" => {
        val nationStrRDD: RDD[String] = sc.textFile(s"$base/nation.tbl")
        val nationRDD: RDD[Nation] = nationStrRDD.map(line => {
          val rows: Array[String] = line.split("[|]")
          Nation(rows(0).toInt, rows(1), rows(2).toInt, rows(3))
        })

        val nationDS: Dataset[Nation] = nationRDD.toDS()
        nationDS.createOrReplaceTempView("nation_tmp")

        spark.sql("insert into table nation select * from nation_tmp")
      }
      case _ => {
        System.err.println("Please set item name from 'customer, lineitem, nation, orders, part, partsupp, region, supplier'")
        System.exit(1)

      }
    }
  }

  def main(args: Array[String]): Unit = {
    Tools.setBasePath(args)
    val spark: SparkSession = SparkSession.builder()
      .appName("InsertTable")
      .master("local[4]")
      .config("spark.debug.maxToStringFields", 200)
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")

    val itemName = if (args.length > 1) args(1) else ""

    CreateTask.main(spark)

    ItemInsertTask.main(spark, itemName)

    spark.stop()
  }

}
