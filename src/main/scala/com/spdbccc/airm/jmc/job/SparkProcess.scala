package com.spdbccc.airm.jmc.job

import cn.hutool.core.date.{DateField, DateUtil}
import org.apache.commons.lang3.time.DateUtils

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.collection.mutable

object SparkProcess {

  def main(args: Array[String]): Unit = {
    //解析参数获得kvmap为 HashMap
    val params_map: mutable.HashMap[String, String] = args.foldLeft(mutable.HashMap.empty[String, String]) {
      case (map, arg) =>
        val parts = arg.split("=")
        if (parts.length == 2) {
          map += (parts(0) -> parts(1))
        }
        map
    }
    //获得任务类的全类名
    val classname = params_map("classname")
    //通过反射实例化任务类
    val clazz = Class.forName(classname)
    val instance = clazz.newInstance()
    //获取参数内的startdate 和enddate 如果没有，则执行当前dt(yyyyMMdd格式)，如果是负数则以当前dt 往前推 再赋值给参数map内
    val dt = params_map.getOrElse("dt", "0").toInt
    val startdate = params_map.getOrElse("startdate", dt.toString)
    val enddate = params_map.getOrElse("enddate", dt.toString)
    //如果startdate>enddate则交换两个值
    val startdate1 = if (startdate.toInt > enddate.toInt) enddate else startdate
    val enddate1 = if (startdate.toInt > enddate.toInt) startdate else enddate
    //计算两个日期范围内的所有dt 循环执行process
    DateUtil.rangeToList(DateUtils.parseDate(startdate1, "yyyyMMdd"), DateUtils.parseDate(enddate1, "yyyyMMdd"),DateField.DAY_OF_YEAR).map(DateUtil.format(_, "yyyyMMdd")).foreach(dt => {
      //将dt的值改为循环的日期
      params_map("dt") = dt
      //调用任务类的process方法
      instance.asInstanceOf[SparkProcessTrait].process(params_map)
    })
  }

}
