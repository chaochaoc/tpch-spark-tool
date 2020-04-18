var host = "server1"
var port = 11810
var username = "sdbadmin"
var password = "sdbadmin"
var domainName = "domain1"
var collectionSpaceName = "spark_test1"

var db = new Sdb(host, port, username, password)

db.createCS(collectionSpaceName, {Domain: domainName, PageSize: 4096});


db[collectionSpaceName].createCL("lineitem", {
    IsMainCL: true,
    ShardingKey: {l_receiptdate: 1},
    ShardingType: "range"
})

db.createCS(collectionSpaceName + "_lineitem", {Domain: domainName, PageSize: 4096})

for (var i = 1990; i < 2000; i++) {
    db[collectionSpaceName + "_lineitem"].createCL("year_" + i,{
        ShardingKey: {l_orderkey: 1, l_partkey: 1, l_suppkey: 1},
        ShardingType: "hash",
        Partition: 4096
    })
}
for (var i = 1990; i < 2000; i++) {
    db[collectionSpaceName].lineitem.attachCL(collectionSpaceName + "_lineitem" + ".year_" + i, {
        LowBound: {l_receiptdate:  i + "-01-01" },
        UpBound: {l_receiptdate: (i + 1) + "-01-01" }
    })
}

var item = ['orders','partsupp']
for (var i in item){
    db[collectionSpaceName].createCL(item[i], {
        ShardingKey: {o_orderkey: 1, o_custkey: 1},
        ShardingType: "hash",
        Partition: 4096
    })
}

var item = ['part','customer', "supplier"]
for (var i in item){
    db[collectionSpaceName].createCL(item[i], {
        ShardingKey: {o_orderkey: 1, o_custkey: 1},
        ShardingType: "hash",
        Partition: 2048
    })
}

var item = ['nation','region']
for (var i in item) {
    db[collectionSpaceName].createCL(item[i])
}

