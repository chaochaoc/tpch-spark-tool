#!/bin/bash


TPCH_HOME_BIN=$(cd $(dirname $0); pwd)
cd $TPCH_HOME_BIN
TPCH_HOME=$(dirname $(pwd))

if [ $SPARK_HOME ]
then 
$SPARK_HOME/bin/spark-submit --master local[4] --executor-memory 512m --total-executor-cores 2 --jars $TPCH_HOME/lib/spark-sequoiadb_2.11-3.2.4.jar,$TPCH_HOME/lib/sequoiadb-driver-3.2.4.jar --class com.sequoiadb.ItemInsertTask $TPCH_HOME/lib/sparksql_tcph-1.0.jar $TPCH_HOME $1 &&
echo -e "\033[1;32m Sucessful import data to sdb \033[0m"
else
echo -e "\033[1;31m Please set SPARK_HOME \033[0m"
fi
