package com.spdbccc.airm.jmc.datahouse

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}

object LineageAnalyzer {
  def main(args: Array[String]): Unit = {
    // 创建SparkSession
    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("LineageAnalyzer")
      .getOrCreate()
    spark.sparkContext.setLogLevel("warn")
    // 加载数据到DataFrame中
    val df1 = spark.read
      .format("csv")
      .option("header", "true")
      .load("H:\\code\\1myproject\\git\\jmc-spark-module\\data\\datahouse\\DatqQ.csv")

    // 转换数据
    val df2 = transformData(df1)

    // 输出血缘关系
    val lineage = getLineage(df2)
    lineage.show(false)

    spark.stop()
  }

  def transformData(df: DataFrame): DataFrame = {
    df.select(
      col("col1"),
      col("col2").cast("double")
    )
  }

  def getLineage(df: DataFrame): DataFrame = {
    val originCols = df.schema.fieldNames
    val lineage = originCols.foldLeft(df)((df, colName) => {
      df.withColumn(colName, col(colName).alias(s"${colName}_origin"))
    })
    lineage
  }
}
