package com.spdbccc.airm.jmc.function

import org.apache.spark.sql.{Encoder, SparkSession}
import org.apache.spark.sql.expressions.Aggregator

object SparkFunction {
  //通过参数ss:Sparksession tablename columnstr建表，如果表存在则不用建表
  def createTable(ss:SparkSession,tablename:String,columnstr:String): Unit ={
    //如果table为空则建表
    if(!ss.catalog.tableExists(tablename)){
      val sql =
        s"""
           |create table if not exists $tablename ($columnstr)
           |partitioned by (dt string)
           |stored as orcfile
           |location '/user/hive/warehouse/$tablename'
           |tblproperties('orc.compress'='SNAPPY')
           |""".stripMargin
      println("sql:" + sql)
      ss.sql(sql)
    }
  }
  //通过参数ss:Sparksession sql tablename  dt 先执行sql创建dataframe临时表 再根据表结构利用上面方法建表 然后通过sql 插入覆盖这张表的dt分区
  def insertTable(ss:SparkSession,sql:String,tablename:String,dt:String): Unit ={
    ss.sql(sql).createOrReplaceTempView("tmp")
    val columnstr = ss.sql(s"desc tmp").collect().map(_.getAs[String]("col_name")).mkString(",")
    createTable(ss,tablename,columnstr)
    val sql2 =
      s"""
         |insert overwrite table $tablename partition(dt='$dt')
         |select * from tmp
         |""".stripMargin
    println("sql2:" + sql2)
    ss.sql(sql2)
  }

  //将某个dataframe所有字段格式转换为string
  def dfAllToType(df:org.apache.spark.sql.DataFrame,transtype:String="string"):org.apache.spark.sql.DataFrame = {
    val newSchema = df.schema.map(x => x.name -> transtype).toMap
    df.select(df.columns.map(c => df.col(c).cast(newSchema(c))): _*)
  }

  //写一个udaf函数，创建一个aggregator，in使用string，buf使用hutool的jsonobject,out使用string,in来一条在json中创建key value， 如果已经有了加1，out返回json.tostring
  //注意udaf的buffer是个可变变量，所以要使用buffer.update方法，这样才能在udaf中更新buffer
  def createAggregator(): Aggregator[String, String, String] = {
    new Aggregator[String, String, String] {
      override def zero: String = "{}"

      override def reduce(b: String, a: String): String = {
        import cn.hutool.json.JSONUtil
        val json = JSONUtil.parseObj(b)
        if (json.containsKey(a)) {
          json.set(a, json.getInt(a) + 1)
        } else {
          json.set(a, 1)
        }
        json.toString
      }

      override def merge(b1: String, b2: String): String = {
        import cn.hutool.json.JSONUtil
        val json1 = JSONUtil.parseObj(b1)
        val json2 = JSONUtil.parseObj(b2)
        json2.keySet().toArray().map(_.toString).foreach(key => {
          if (json1.containsKey(key)) {
            json1.set(key, json1.getInt(key) + json2.getInt(key))
          } else {
            json1.set(key, json2.getInt(key))
          }
        })
        json1.toString
      }

      override def finish(reduction: String): String = reduction

      override def bufferEncoder: Encoder[String] = org.apache.spark.sql.Encoders.STRING

      override def outputEncoder: Encoder[String] = org.apache.spark.sql.Encoders.STRING
    }
  }

}
