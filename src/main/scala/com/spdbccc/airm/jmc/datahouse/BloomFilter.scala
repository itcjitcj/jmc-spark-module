package com.spdbccc.airm.jmc.datahouse
import org.apache.spark.sql.SparkSession
object BloomFilter {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local").appName("spark sql2").getOrCreate()

    val data = spark.sparkContext.textFile("D:\\idea\\projects\\scalaDemo\\src\\resources\\node.txt").map(_.split(" "))

    val df_ = data.map(s => schema_info(s(0).toInt, s(1).trim(), s(2).toInt))

    import spark.sqlContext.implicits._
    var df = df_.toDF
    df.show()

    val df1 = spark.sparkContext.parallelize(Seq(
      Worker("Braylon",30000), Worker("Tim",1000), Worker("Jackson",20000)
    )).toDF
    df1.show(false)

    val rdd = spark.sparkContext.parallelize(Seq("Braylon","J.C","Neo"))

    // 生成bloomFilter
    val bf = df1.stat.bloomFilter("name",20L,0.01)
    val res = rdd.map(x=>(x,bf.mightContainString(x)))
    res.foreach(println)
  }

}

case class Worker(name:String,Sal:Int)
case class schema_info(col1:Int,col2:String,col3:Int)