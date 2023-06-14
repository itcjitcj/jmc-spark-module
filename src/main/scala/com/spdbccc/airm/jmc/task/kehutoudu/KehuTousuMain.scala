package com.spdbccc.airm.jmc.task.kehutoudu

import com.huaban.analysis.jieba.JiebaSegmenter
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.udf

import scala.io.Source

object KehuTousuMain {

  def main(args: Array[String]): Unit = {

  }

  def process(ss:SparkSession): Unit = {
    val df = ss.read.csv("")
  }

//  //写一个udf方法 先替换客户 坐席 空格 为。 然后用jieba进行lcut 再读取stopwords.txt 进行过滤
//  //最后输出一个分词后的结果
//    def jieba_udf = udf((str: String) => {
//      val str1 = str.replaceAll("客户|坐席| ", "。")
//      val lcut = JiebaSegmenter.getJiebaSegmenter().lcut(str1, false)
//      val stopwords = Source.fromFile("stopwords.txt").getLines().toSet
//      lcut.filter(!stopwords.contains(_)).mkString(";")
//    })

  //写一个udf方法 先替换客户 坐席 空格 为。 然后用jieba进行lcut 再读取stopwords.txt 进行过滤

}
