package com.spdbccc.airm.jmc.task.kehutoudu

import com.spdbccc.airm.jmc.client.SparkSessionClient

object TestCsv {

  def main(args: Array[String]): Unit = {
    val ss = SparkSessionClient.getSparkSession("apptest")
    val df = ss.read.option("header", "true").option("delimiter", "~|~").csv("file:///H:\\code\\1myproject\\jmc-spark-module\\data\\a.csv")
    //将第二列求和
//    df.createOrReplaceTempView("a")
//    val sql =
//      """
//        |select sum(cast(_c1 as int)) from a
//        |""".stripMargin
//    val df2 = ss.sql(sql)
    df.show()

  }

}
