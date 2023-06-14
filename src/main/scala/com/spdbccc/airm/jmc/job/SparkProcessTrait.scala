package com.spdbccc.airm.jmc.job

import scala.collection.mutable

trait SparkProcessTrait {

  //针对spark任务设计一个执行模板接口
  def process(params_map: mutable.HashMap[String, String] ):Unit

}
