package com.sequoiadb.sql

import com.sequoiadb.utils.Tools

object DDL {


  def customer: String =
    s"""CREATE TABLE IF NOT EXISTS customer (
       |c_custkey    int,
       |c_name       string,
       |c_address    string,
       |c_nationkey  int,
       |c_phone      string,
       |c_acctbal    decimal(15,2),
       |c_mktsegment string,
       |c_comment    string )
     |using com.sequoiadb.spark options (
       |host '${Tools.host}:${Tools.port}',
       |username '${Tools.username}',
       |password '${Tools.password}',
       |collectionspace '${Tools.collectionSpace}',
       |collection 'customer',
       |bulksize '${Tools.bulksize}'
     |)""".stripMargin

  def lineitem: String =
    s"""CREATE TABLE IF NOT EXISTS lineitem (
       |l_orderkey      int,
       |l_partkey       int,
       |l_suppkey       int,
       |l_linenumber    int,
       |l_quantity      decimal(15,2),
       |l_extendedprice decimal(15,2),
       |l_discount      decimal(15,2),
       |l_tax           decimal(15,2),
       |l_returnflag    string,
       |l_linestatus    string,
       |l_shipdate      string,
       |l_commitdate    string,
       |l_receiptdate   string,
       |l_shipinstruct  string,
       |l_shipmode      string,
       |l_comment       string )
     |using com.sequoiadb.spark options (
       |host '${Tools.host}:${Tools.port}',
       |username '${Tools.username}',
       |password '${Tools.password}',
       |collectionspace '${Tools.collectionSpace}',
       |collection 'lineitem',
       |bulksize '${Tools.bulksize}'
     |)""".stripMargin

  def nation: String =
    s"""CREATE TABLE IF NOT EXISTS nation (
       |n_nationkey int,
       |n_name      string,
       |n_regionkey int,
       |n_comment   string )
     |using com.sequoiadb.spark options (
       |host '${Tools.host}:${Tools.port}',
       |username '${Tools.username}',
       |password '${Tools.password}',
       |collectionspace '${Tools.collectionSpace}',
       |collection 'nation',
       |bulksize '${Tools.bulksize}'
     |)""".stripMargin

  def orders: String =
    s"""CREATE TABLE IF NOT EXISTS orders (
       |o_orderkey       int,
       |o_custkey        int,
       |o_orderstatus    string,
       |o_totalprice     decimal(15,2),
       |o_orderdate      string,
       |o_orderpriority  string,
       |o_clerk          string,
       |o_shippriority   int,
       |o_comment        string )
     |using com.sequoiadb.spark options (
       |host '${Tools.host}:${Tools.port}',
       |username '${Tools.username}',
       |password '${Tools.password}',
       |collectionspace '${Tools.collectionSpace}',
       |collection 'orders',
       |bulksize '${Tools.bulksize}'
     |)""".stripMargin

  def part: String =
    s"""CREATE TABLE IF NOT EXISTS part (
       |p_partkey     int,
       |p_name        string,
       |p_mfgr        string,
       |p_brand       string,
       |p_type        string,
       |p_size        int,
       |p_container   string,
       |p_retailprice decimal(15,2),
       |p_comment     string )
     |using com.sequoiadb.spark options (
       |host '${Tools.host}:${Tools.port}',
       |username '${Tools.username}',
       |password '${Tools.password}',
       |collectionspace '${Tools.collectionSpace}',
       |collection 'part',
       |bulksize '${Tools.bulksize}' )""".stripMargin

  def partsupp: String =
    s"""CREATE TABLE IF NOT EXISTS partsupp (
       |ps_partkey    int,
       |ps_suppkey    int,
       |ps_availqty   int,
       |ps_supplycost decimal(15,2),
       |ps_comment    string )
     |using com.sequoiadb.spark options (
       |host '${Tools.host}:${Tools.port}',
       |username '${Tools.username}',
       |password '${Tools.password}',
       |collectionspace '${Tools.collectionSpace}',
       |collection 'partsupp',
       |bulksize '${Tools.bulksize}' )""".stripMargin

  def region: String =
    s"""CREATE TABLE IF NOT EXISTS region (
       |r_regionkey  int,
       |r_name       string,
       |r_comment    string )
     |using com.sequoiadb.spark options (
       |host '${Tools.host}:${Tools.port}',
       |username '${Tools.username}',
       |password '${Tools.password}',
       |collectionspace '${Tools.collectionSpace}',
       |collection 'region',
       |bulksize '${Tools.bulksize}'
     |)""".stripMargin

  def supplier: String =
    s"""CREATE TABLE IF NOT EXISTS supplier (
       |s_suppkey   int,
       |s_name      string,
       |s_address   string,
       |s_nationkey int,
       |s_phone     string,
       |s_acctbal   decimal(15,2),
       |s_comment   string )
     |using com.sequoiadb.spark options (
       |host '${Tools.host}:${Tools.port}',
       |username '${Tools.username}',
       |password '${Tools.password}',
       |collectionspace '${Tools.collectionSpace}',
       |collection 'supplier',
       |bulksize '${Tools.bulksize}'
     |)""".stripMargin

  def main(args: Array[String]): Unit = {
    println(lineitem.replaceAll("\r\n", " "))
    com.sequoiadb.spark.SequoiadbRelation
  }
}