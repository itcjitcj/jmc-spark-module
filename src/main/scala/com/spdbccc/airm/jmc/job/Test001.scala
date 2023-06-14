package com.spdbccc.airm.jmc.job
import scala.collection.mutable

class Test001 extends SparkProcessTrait {
  override def process(params_map: mutable.HashMap[String, String]): Unit = {
    println(params_map("dt"))
  }
}
