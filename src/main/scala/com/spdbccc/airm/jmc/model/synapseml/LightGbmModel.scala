package com.spdbccc.airm.jmc.model.synapseml

import org.apache.spark.sql.SparkSession

object LightGbmModel {

  def main(args: Array[String]): Unit = {

  }

  def trainModel(ss:SparkSession): Unit = {
    // 读取数据
    val df = ss.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(
        "wasbs://publicwasb@mmlspark.blob.core.windows.net/company_bankruptcy_prediction_data.csv"
      )
    // 将数据分为训练集和测试集
    val dfSplit = df.randomSplit(Array(0.85, 0.15), seed = 1)
    dfSplit(0).createOrReplaceTempView("train")
    dfSplit(1).createOrReplaceTempView("test")
    // 添加特征化器以将特征转换为矢量
    import org.apache.spark.ml.feature.VectorAssembler
    val assembler = new VectorAssembler()
      .setInputCols(df.columns.filter(_ != "Bankrupt?"))
      .setOutputCol("features")
    // 添加LightGBM分类器
//    import com.microsoft.azure.synapse.ml.lightgbm.LightGBMClassifier
//    val lgbm = new LightGBMClassifier()
//      .setLabelCol("Bankrupt?")
//      .setFeaturesCol("features")
//      .setNumLeaves(10)
//      .setNumIterations(100)
//      .setLearningRate(0.1)
//      .setObjective("binary")
  }

}
