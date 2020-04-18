

## TPC-H自动化测试使用文档

#### 概述

​	本脚本使用tpch中的dbgen，dqgen等工具生成数据，使用spark-sql完成tpch任务。



#### 环境要求

- JDK 1.8
- spark-2.3.4-bin-hadoop2.7



#### Spark环境搭建（建议worker数与sdb集群的机器数一致）

**本例中使用192.168.50网段的11,12,13三台主机（主机名server1, server2, server3）, 搭建三worker节点**

**下列步骤没有特殊声明的，请在server1上执行**

- 安装JDK1.8， 配置环境变量。 步骤太过于简单，略

- 配置hosts文件 

```shell
cat >> /etc/hosts <<EOF
192.168.50.11 server1
192.168.50.12 server2
192.168.50.13 server3
EOF
```



- 配置三台主机免密钥登录，建议使用sdbadmin用户进行配置

```shell
# 注： 下列命令请在所有主机上执行

# 密钥生成
ssh-keygen -t rsa
# 拷贝公钥
ssh-copy-id server1
```

```shell
# 注： 下列命令请在 server1 主机上执行

# 分发公钥文件, 请在家目录下执行
scp .ssh/authorized_keys server2:$PWD/.ssh
scp .ssh/authorized_keys server3:$PWD/.ssh

# 配置成功后可使用 ssh 用户名@主机名验证免密登录是否配置成功
# 如果成功则不需要输入密码，如
ssh sdbadmin@server2
```



- 下载spark安装包

```shell
wget https://www.apache.org/dyn/closer.lua/spark/spark-2.3.4/spark-2.3.4-bin-hadoop2.7.tgz
```



- 解压

```
tar -zxvf spark-2.3.4-bin-hadoop2.7.tgz
```



- 修改配置文件

```shell
cd you_spark_install_path/conf

cp spark-env.sh.template spark-env.sh

cat >> spark-env.sh <<EOF
# 请修改为你的jdk安装路径
export JAVA_HOME=/opt/service/jdk1.8.0_221
# master节点
export SPARK_MASTER_HOST=server1
# master管理端口
export SPARK_MASTER_PORT=7077
# worker内存大小,按需要设置
export SPARK_WORKER_MEMORY=128g
# worker核数,按需要设置
export SPARK_WORKER_CORES=25
EOF

cp slaves.template slaves
# 添加worker节点主机名，请按需修改
cat > slaves <<EOF
server1
server2
server3
EOF
```



- 安装包分发到其他节点

```\
scp -r spark-2.3.4-bin-hadoop2.7 sdbadmin@server2:$PWD
scp -r spark-2.3.4-bin-hadoop2.7 sdbadmin@server3:$PWD
```



- 配置Spark环境变量

```shell
# 下列操作请在所有主机上执行

cat >> ~/.bash <<EOF
# 请修改路径
export SPARK_HOME=you_spark_install_path
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
EOF

# 刷新配置使生效
. ~/.bash
```



- 启动集群

```
spark-all.sh
```



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

