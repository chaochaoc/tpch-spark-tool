#!/bin/bash

TPCH_HOME_BIN=$(cd $(dirname $0); pwd)
cd $TPCH_HOME_BIN
TPCH_HOME=$(dirname $(pwd))
cd $TPCH_HOME
which sdb > /dev/null
if [ $? -eq 0 ]
then
host=`grep sdb.host conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
port=`grep sdb.port conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
username=`grep sdb.username conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
password=`grep sdb.password conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
collectionSpaceName=`grep sdb.collection.space conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
domainName=`grep sdb.domain.name conf/sdb.properties | cut -d'=' -f2 | sed 's/\r//'`
sed -i -e 's/^var host.*$/var host = \"'$host\"'/g' conf/create.js
sed -i -e 's/^var port.*$/var port = '$port'/g' conf/create.js
sed -i -e 's/^var username.*$/var username = \"'$username\"'/g' conf/create.js
sed -i -e 's/^var password.*$/var password = \"'$password\"'/g' conf/create.js
sed -i -e 's/^var collectionSpaceName.*$/var collectionSpaceName = \"'$collectionSpaceName\"'/g' conf/create.js
sed -i -e 's/^var domainName.*$/var domainName = \"'$domainName\"'/g' conf/create.js
sdb -f conf/create.js &&
echo -e "\033[1;32m Successful execute all tasks \033[0m"
else
echo -e "\033[1;31m Please use sdbadmin to action, 'sdb' command not found \033[0m"
fi 
