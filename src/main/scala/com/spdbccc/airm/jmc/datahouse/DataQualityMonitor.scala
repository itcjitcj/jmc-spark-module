package com.spdbccc.airm.jmc.datahouse

import org.apache.spark.sql.{Column, DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.JavaConverters._

object DataQualityMonitor {
  def main(args: Array[String]): Unit = {
    // 创建SparkSession
    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("DataQualityMonitor")
      .getOrCreate()
    spark.sparkContext.setLogLevel("warn")
    // 加载数据到DataFrame中
    val df = spark.read.format("csv")
      .option("header", "true")
      .load("H:\\code\\1myproject\\git\\jmc-spark-module\\data\\datahouse\\DatqQ.csv")
    df.show()
    // 定义数据质量指标
    val completeness = checkCompleteness(df, Seq("col1", "col2"))
    completeness.show()
    val uniqueness = checkUniqueness(df, Seq("col1", "col2"))
    uniqueness.show()
    val format = checkDateFormat(df, "date_col1", "yyyy-MM-dd")
    format.show()
    val range = checkRange(df, "num_col1", 0, 100)
    range.show()

    // 将结果存储到HDFS或关系型数据库中
    completeness.write.mode(SaveMode.Overwrite).format("parquet").save("H:\\code\\1myproject\\git\\jmc-spark-module\\data\\datahouse\\completeness\\parquet")
    uniqueness.write.mode(SaveMode.Overwrite).format("parquet").save("H:\\code\\1myproject\\git\\jmc-spark-module\\data\\datahouse\\uniqueness\\parquet")
    format.write.mode(SaveMode.Overwrite).format("parquet").save("H:\\code\\1myproject\\git\\jmc-spark-module\\data\\datahouse\\format\\parquet")
    range.write.mode(SaveMode.Overwrite).format("parquet").save("H:\\code\\1myproject\\git\\jmc-spark-module\\data\\datahouse\\range\\parquet")

    spark.stop()
  }

  // 检查数据完整性
  def checkCompleteness(df: DataFrame, columns: Seq[String]): DataFrame = {
    val column1: Column = count("*").alias("total_rows")
    val columns1: Seq[Column] = columns.map(colName => count(col(colName)).alias(s"${colName}_not_null"))
    //获取总的列数，以及每列非空的行数
    df.select(
      column1 +: columns1: _*
    )
  }

  // 检查数据唯一性
  def checkUniqueness(df: DataFrame, columns: Seq[String]): DataFrame = {
    val column1 = countDistinct(col(columns.head)).alias(s"distinct_${columns.head}")
val columns1 = columns.tail.map(colName => countDistinct(col(colName)).alias(s"distinct_${colName}"))
    df.select(
      column1 +: columns1: _*
    )
  }

  // 检查日期格式
  def checkDateFormat(df: DataFrame, column: String, format: String): DataFrame = {
    df.select(
      count("*").alias("total_rows"),
      count(when(to_date(col(column), format).isNull, column)).alias(s"${column}_invalid")
    )
  }

  // 检查数值范围
  def checkRange(df: DataFrame, column: String, min: Double, max: Double): DataFrame = {
    df.select(
      count("*").alias("total_rows"),
      count(when(col(column) < min || col(column) > max, column)).alias(s"${column}_out_of_range")
    )
  }
}
