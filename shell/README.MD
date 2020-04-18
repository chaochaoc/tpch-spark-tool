

## TPC-H自动化测试使用文档

#### 概述

​	本脚本使用tpch中的dbgen，dqgen等工具生成数据，使用spark-sql完成tpch任务。



#### 环境要求

- JDK 1.8
- spark-2.3.4-bin-hadoop2.7



#### 测试步骤

- 解压tpc_h.tar.gz

```shell
tar -zxvf tpc_h.tar.gz
```



- 初始化sdb环境  (执行此命令的用户需要为sdb管理员用户，例如 sdbadmin)

```shell
cd tpc_h

# 编辑conf/sdb.properties
vim conf/sdb.properties

bin/sdb_init.sh
```



- sdb参数配置详解

```properties
# 测试主机地址
sdb.host=192.168.10.11
# 协调节点端口号
sdb.port=11810
# 用户名
sdb.username=sdbadmin
# 密码
sdb.password=sdbadmin
# 测试使用的域名，需要提前创建
sdb.domain.name=domain1
# 设置的集合空间名，需要保证集合空间不存在
sdb.collection.space=spark_test1

# 数据的批量
spark.bulk.size=2048
# master url
spark.master.url=spark://node1:7077
# 执行测试所用的executor内存
spark.cluster.task.executor.memory=16g
# 执行测试所用的总核数
spark.cluster.task.total.executor.cores=150
# 每个executor的核数
spark.cluster.task.executor.cores=25
```



- 数据生成

```shell
# 默认生成的数据量为 1G
bin/start-gen.sh

# 如果需要调整, 下面的命令表示生成100G的数据
# 可取的数据量的为: 1 10 100 300 1000 3000 10000 30000 100000
bin/start-gen.sh 100
```



- 数据导入  (执行此命令的用户需要配置SPARK_HOME变量)

```shell
# 启动spark导入数据，需要保证测试机空闲内存大于 8G
bin/start-import.sh

# 如果数据量较大，需要逐项导入则使用下面的命令
# 可取的参数值的为: customer, lineitem, nation, orders, part, partsupp, region, supplier
bin/start-import-item.sh customer

```



- TPCH测试开始 (执行此命令的用户需要配置SPARK_HOME变量)

```shell
bin/start-query.sh

# 程序执行的log在logs下

# 最终结果在result/spark_test_tpch_res.out中
# 结果文件内容为:
# 01:12:54.879 sql [1] start in 1570727574879
# 01:14:49.522 sql [1] finish in 1570727689522, It took 114.643 second altogether.
```

