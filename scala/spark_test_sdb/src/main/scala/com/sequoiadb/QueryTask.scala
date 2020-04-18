package com.sequoiadb

import com.sequoiadb.sql.DQL
import com.sequoiadb.utils.Tools
import org.apache.log4j.PropertyConfigurator
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ArrayBuffer

object QueryTask {


  private val logger: Logger = LoggerFactory.getLogger("query")
  private var start: Long = 0L

  def main(spark: SparkSession, num: Int): Unit = {

    if (num == 0){
      for (i <- 1 to 22){
        start = System.currentTimeMillis()
        logger.info(s"sql [${i}] start in ${start}");
        query(i).foreach(spark.sql(_).show())
        logger.info(s"sql [${i}] finish in ${System.currentTimeMillis()}, It took ${(System.currentTimeMillis() - start) / 1000.0} second altogether.");
        logger.info("*"*50)
      }
    }else {
      start = System.currentTimeMillis()
      logger.info(s"sql [${num}] start in ${start}");
      query(num).foreach(spark.sql(_).show())
      logger.info(s"sql [${num}] finish in ${System.currentTimeMillis()}, It took ${(System.currentTimeMillis() - start) / 1000.0} second altogether.");
      logger.info("*"*50)
    }

  }

  def query(num: Int): Array[String] = {
    val sqls = DQL.querySql(num).split(";").reverse
    val newSql = new ArrayBuffer[String]()
    var status: Boolean = true
    for (i <- 0 to (sqls.length - 1)) {
      if (status) {
        if (sqls(i).contains("LIMIT")) {
          if (!sqls(i).contains("-1")) {
            status = false
            newSql += Array(sqls(i + 1), sqls(i)).mkString(" ")
          }
        } else {
          newSql += sqls(i)
          status = true
        }
      }
    }
    newSql.reverse.toArray
  }

  def main(args: Array[String]): Unit = {

    Tools.setBasePath(args)

    PropertyConfigurator.configure(s"${Tools.basePath}/conf/log4j.properties")

    val spark: SparkSession = SparkSession.builder()
      .appName("QueryTable")
      .master(Tools.master)
      .config("spark.debug.maxToStringFields", 200)
      .getOrCreate()
    spark.sparkContext.setLogLevel("INFO")

    CreateTask.main(spark)

    val num  = if (args.length > 1) args(1).toInt else 0
    QueryTask.main(spark, num)

    spark.stop()
  }

}
