#!/bin/bash

TPCH_HOME_BIN=$(cd $(dirname $0); pwd)
cd $TPCH_HOME_BIN
TPCH_HOME=$(dirname $(pwd))
cd $TPCH_HOME
mkdir -p gen/tbl
mkdir -p gen/sql
cd gen/tbl

if [ $1 ] 
then
size=$1
else 
size=1
fi
if [ -x $TPCH_HOME/tool/dbgen/dbgen ]
then
$TPCH_HOME/tool/dbgen/dbgen -s$size -b $TPCH_HOME/tool/dbgen/dists.dss -f -v &&
echo -e "\033[1;32m Successful data generation, you can found it in '$TPCH_HOME/gen/tbl' \033[0m"
else
echo -e "\033[1;31m Permission denied 'dbgen', please 'chmod u+x $TPCH_HOME/tool/dbgen/dbgen' \033[0m"
fi

cd ../../tool/dbgen/queries/
if [ -x ./qgen ]
then
for i in {1..22}
do
./qgen -d $i > $TPCH_HOME/gen/sql/$i.sql
done && 
echo -e "\033[1;32m Successful sql generation, you can found it in '$TPCH_HOME/gen/sql' \033[0m"
else
echo -e "\033[1;31m Permission denied 'qgen', please 'chmod u+x $TPCH_HOME/tool/dbgen/queries/qgen' \033[0m"
fi
