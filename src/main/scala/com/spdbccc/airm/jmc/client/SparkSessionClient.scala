package com.spdbccc.airm.jmc.client

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.security.UserGroupInformation
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSessionClient {

  //写一个获取sparksession的静态方法
  def getSparkSession(appname:String="mmc_test001",sparkConf:SparkConf = new SparkConf()): SparkSession = {
    sparkConf.setAppName(appname)
    sparkConf.setMaster("local[*]")
    sparkConf.set("spark.sql.crossJoin.enabled", "false")
    sparkConf.set("spark.sql.shuffle.partitions", "20")
    sparkConf.set("spark.default.parallelism", "20")
    sparkConf.set("spark.sql.shuffle.partitions", "20")

    val sparkSession = SparkSession.builder()
      .config(sparkConf)
//      .config("spark.sql.warehouse.dir", "hdfs://hadoop001:9000/user/hive/warehouse")
//      .config("hive.metastore.uris", "thrift://hadoop001:9083")
//      .config("spark.sql.hive.convertMetastoreParquet", "false")
      .enableHiveSupport()
      .getOrCreate()
    sparkSession.sparkContext.setLogLevel("warn")
    sparkSession
  }

  //通过keberos认证获取sparksession
  def getSparkSessionWithKeyberos(appname:String): SparkSession = {
    System.setProperty("sun.security.krb5.debug", "true")
    System.setProperty("sun.security.krb5.conf", "C:\\Users\\Administrator\\Desktop\\krb5.conf")

    //夺取相关hdfs配置并登录keberos认证
    val conf = new Configuration()
    conf.addResource(new Path("C:\\Users\\Administrator\\Desktop\\core-site.xml"))
    conf.addResource(new Path("C:\\Users\\Administrator\\Desktop\\hdfs-site.xml"))
    conf.setBoolean("hadoop.security.authentication", true)
    UserGroupInformation.setConfiguration(conf)
    UserGroupInformation.loginUserFromKeytab("fid_bg_mmc","C:\\Users\\Administrator\\Desktop\\fid_bg_mmc.keytab")

    val sparkConf = new SparkConf()
    val ss = SparkSession.builder().appName(appname).master("local[*]").config(sparkConf)
      //设置hive版本和依赖包位置
      .config("spark.sql.hive.metastore.version", "2.1.1")
      .config("spark.sql.hive.metastore.jars", "C:\\Users\\Administrator\\Desktop\\hive\\lib\\*")
//      .config("spark.sql.storeAssignmentPolicy", "LEGACY")
      //设置hdfs block连接次数
      .config("dfs.client.block.write.locateFollowingBlock.retries", "10")
      //设置兼容过期scala格式
      .config("spark.sql.legacy.allowUntypedScalaUDF", "true")
      //设置字段最长限制2000
      .config("spark.debug.maxToStringFields", "2000")
      .enableHiveSupport().getOrCreate()
    ss.sparkContext.setLogLevel("warn")
    ss
  }

}
