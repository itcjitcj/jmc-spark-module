package com.spdbccc.airm.jmc.datahouse

import io.delta.tables._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object DeltaTest {
  def main(args: Array[String]): Unit = {
    demo()
  }

  def demo(): Unit = {
    // 创建 SparkSession
    val spark = SparkSession.builder()
      .appName("DeltaTest")
      .master("local[*]")
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.databricks.delta.retentionDurationCheck.enabled", "false")
      .enableHiveSupport()
      .getOrCreate()
    spark.sparkContext.setLogLevel("warn")
    //创建 DataFrame
    val data = Seq((1, "foo"), (2, "bar"), (3, "baz"))
    val df: DataFrame = spark.createDataFrame(data).toDF("id", "name")
    //读取路径创建临时表
    df.write.format("delta").mode(SaveMode.Overwrite).save("H:\\data\\spark\\table\\DeltaTest")
    val deltadf = spark.read.format("delta").load("H:\\data\\spark\\table\\DeltaTest")
    deltadf.show()
//    deltadf.createOrReplaceTempView("test_table")
    //将 DataFrame 保存到 Delta 表中
    df.write.format("delta").saveAsTable("test_table")

    //向 Delta 表中插入新数据
    val newRow = Seq((4, "qux"))
    val newDf: DataFrame = spark.createDataFrame(newRow).toDF("id", "name")

    val deltaTable: DeltaTable = DeltaTable.forName(spark, "test_table")
    deltaTable.alias("oldData").merge(
      newDf.alias("newData"),
      "newData.id = oldData.id")
      .whenNotMatched().insertExpr(Map("id" -> "newData.id", "name" -> "newData.name"))
      .execute()
    //从 Delta 表中检索数据
    val deltaTable2 = DeltaTable.forName(spark, "test_table")
    val data2 = deltaTable2.toDF
    data2.show()
  }
}
