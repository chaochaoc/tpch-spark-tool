package com.sequoiadb.utils

import java.io.{File, FileInputStream}
import java.util.Properties

import com.sequoiadb.sql.DQL

import scala.io.Source

object Tools {

  var basePath = ""
  private val prop: Properties = new Properties()

  def readFileForLine(path: String): String ={
    val source = Source.fromFile(path, "UTF-8")
    val lines = source.getLines().toArray.filter(line => {
      !line.contains("--") & !line.isEmpty
    })
    source.close()
    lines.mkString(" ")
  }

  def setBasePath (args: Array[String]): String = {
    if (args.length > 0){
      basePath = args(0)
      prop.load(new FileInputStream(s"${basePath}/conf/sdb.properties"))
    }
    else {
//      basePath = "C:\\javawork\\spark_parent\\tcph_sdb_init\\src\\main\\resources"
      println("no set basePath")
    }
    ""
  }

  def host = prop.getProperty("sdb.host", "127.0.0.1")

  def port = prop.getProperty("sdb.port", "11810").toInt

  def username = prop.getProperty("sdb.username", "sdbadmin")

  def password = prop.getProperty("sdb.password", "sdbadmin")

  def collectionSpace = prop.getProperty("sdb.collection.space", "tpch_test")

  def bulksize = prop.getProperty("spark.bulk.size", "2048")

  def master: String = prop.getProperty("spark.master.url", "spark://127.0.0.1:7077")

  def main(args: Array[String]): Unit = {
    val test = readFileForLine("C:\\javawork\\spark_parent\\tcph_sdb_init\\src\\main\\resources\\gen\\sql\\7.sql")
    println(test)
  }

}
