package com.sequoiadb

import org.apache.spark.sql.SparkSession
import com.sequoiadb.sql.DDL

object CreateTask {
  def main(spark: SparkSession): Unit = {
    // 用户表
    spark.sql(DDL.customer)
    // 在线商品
    spark.sql(DDL.lineitem)
    // 国家
    spark.sql(DDL.nation)
    // 订单
    spark.sql(DDL.orders)
    // 零件
    spark.sql(DDL.part)
    // 供货商的零件
    spark.sql(DDL.partsupp)
    // 地区
    spark.sql(DDL.region)
    // 供货商
    spark.sql(DDL.supplier)

  }
}
