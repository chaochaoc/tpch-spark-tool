#!/bin/bash

TPCH_HOME_BIN=$(cd $(dirname $0); pwd)
cd $TPCH_HOME_BIN
TPCH_HOME=$(dirname $(pwd))
cd $TPCH_HOME

masterUrl=`grep spark.master.url conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
executorMemory=`grep spark.cluster.task.executor.memory conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
totalExecutorCores=`grep spark.cluster.task.total.executor.cores conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
executorCores=`grep spark.cluster.task.executor.cores conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`

sed -i -e 's#^log4j.appender.file.File.*#log4j.appender.file.File='$TPCH_HOME'/logs/tpch.log#g' $TPCH_HOME/conf/log4j.properties
sed -i -e 's#^log4j.appender.query.File.*#log4j.appender.query.File='$TPCH_HOME'/result/spark_test_tpch_res.out#g'  $TPCH_HOME/conf/log4j.properties

if [ -f $TPCH_HOME/result/spark_test_tpch_res.out ]
then 
mv $TPCH_HOME/result/spark_test_tpch_res.out $TPCH_HOME/result/spark_test_tpch_res.out.`date '+%Y%m%d%H%M%S'`
fi

if [ $SPARK_HOME ]
then
$SPARK_HOME/bin/spark-submit --master $masterUrl --executor-memory $executorMemory --total-executor-cores $totalExecutorCores --executor-cores $executorCores --jars $TPCH_HOME/lib/spark-sequoiadb_2.11-3.2.4.jar,$TPCH_HOME/lib/sequoiadb-driver-3.2.4.jar --class com.sequoiadb.QueryTask $TPCH_HOME/lib/sparksql_tcph-1.0.jar $TPCH_HOME &&
echo -e "\033[1;32m Successful executer sql, you can found result in '$TPCH_HOME/result/' \033[0m"
else
echo -e "\033[1;31m Please set SPARK_HOME \033[0m"
fi
