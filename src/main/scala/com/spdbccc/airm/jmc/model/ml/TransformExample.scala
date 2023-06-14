package com.spdbccc.airm.jmc.model.ml

import org.apache.spark.sql.SparkSession

object TransformExample {

  def main(args: Array[String]): Unit = {
    testSqlTransformer()
  }

  //测试sqltransformer
  def testSqlTransformer(): Unit = {
    import org.apache.spark.ml.feature.SQLTransformer
//    System.setProperty("hadoop.home.dir", "H:\\code\\1yuanma\\winutils\\hadoop-2.7.4")
    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("SQLTransformerExample")
      .getOrCreate()
    spark.sparkContext.setCheckpointDir("H:\\data\\spark\\checkpoint\\TransformExample")

    // $example on$
    val df = spark.createDataFrame(
      Seq((0, 1.0, 3.0), (2, 2.0, 5.0))).toDF("id", "v1", "v2")
    df.checkpoint()
    val sqlTrans = new SQLTransformer().setStatement(
      "SELECT *, (v1 + v2) AS v3, (v1 * v2) AS v4 FROM __THIS__")

    sqlTrans.transform(df).show()
    // $example off$

    spark.stop()
  }


}
