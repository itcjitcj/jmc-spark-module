package com.spdbccc.airm.jmc.model.pmml

import com.spdbccc.airm.jmc.client.SparkSessionClient
import com.spdbccc.airm.jmc.function.SparkFunction
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder

import java.io.File
import java.nio.file.Files
import javax.xml.transform.stream.StreamSource


object PmmlModel {
  def main(args: Array[String]): Unit = {
    val ss = SparkSessionClient.getSparkSession("apptest")
    trainModel(ss)
  }

  //定义一个方法，里面读取model.csv 的数据，然后训练一个模型，然后保存到pmml文件中
  def trainModel(ss: SparkSession): Unit = {

    // 读取数据
    val dataTemp = ss.read.option("header", "true").option("delimiter", ",").csv("file:///H:\\code\\1myproject\\jmc-spark-module\\data\\model.csv")
    val data=SparkFunction.dfAllToType(dataTemp,"double")
    // 准备训练数据

    //多个特征合并
    val assembler = new VectorAssembler()
      .setInputCols(Array("c1", "c2", "c3"))
      .setOutputCol("features")
//    val inputData = assembler.transform(data)
    // 构建回归模型
    val model = new LogisticRegression()
      .setFeaturesCol("features")
      .setLabelCol("y")
      .setPredictionCol("prediction")
    //将特征和模型组合成管道
    val pipeline = new Pipeline().setStages(Array(assembler,model))
    // 训练模型
    val trainedModel = pipeline.fit(data)
    // 使用模型进行预测
    trainedModel.transform(data).show(10)
    // 将模型保存为 PMML 文件
    import org.jpmml.model.JAXBUtil
    import org.jpmml.sparkml.PMMLBuilder

    import javax.xml.transform.stream.StreamResult
    val pmml = new PMMLBuilder(data.schema, trainedModel).build()
    // 打印 PMML 文件
    JAXBUtil.marshalPMML(pmml, new StreamResult(System.out))
    // 保存 PMML 文件
    val pmmlFile = new File("H:\\code\\1myproject\\jmc-spark-module\\data\\pmml\\model.xml")
    JAXBUtil.marshalPMML(pmml,new StreamResult(Files.newOutputStream(pmmlFile.toPath)))
    // 读取 PMML 文件
    val pmml2 = JAXBUtil.unmarshalPMML(new StreamSource(Files.newInputStream(pmmlFile.toPath)))
    // 使用 PMML 文件进行预测
    val evaluator = new LoadingModelEvaluatorBuilder().load(pmmlFile).build();
    evaluator.verify();
    // 打印模型输入信息
    val inputFields = evaluator.getInputFields
    System.out.println("Inputfields : " + inputFields)

    // 打印模型输出信息
    val targetFields = evaluator.getTargetFields
    System.out.println("Target field(s): " + targetFields)
    val outputFields = evaluator.getOutputFields
    System.out.println("Output fields: " + outputFields)
    // 指定模型输入数据
    val arguments = new java.util.HashMap[String, Any]
    arguments.put("c1", 1.0)
    arguments.put("c2", 2.0)
    arguments.put("c3", 3.0)
    // 执行模型预测
    val result = evaluator.evaluate(arguments)
    System.out.println("Result: " + result)
    // 打印模型输出
    val targetField = targetFields.get(0)
    val targetValue = result.get(targetField)
    System.out.println("Target: " + targetValue)
  }
}
