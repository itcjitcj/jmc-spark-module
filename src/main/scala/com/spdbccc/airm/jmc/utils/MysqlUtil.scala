package com.spdbccc.airm.jmc.utils

import java.sql.DriverManager
import java.util.Properties
import scala.collection.JavaConverters._

object MysqlUtil {
  private val config = new Properties()
  config.load(this.getClass.getClassLoader.getResourceAsStream("config.properties"))
  Class.forName(config.getProperty("mysql.driver"))

  //通过config获取mysql的connect 然后传入sql 执行sql 并通过反射获取结果对象值
  def queryList[T](sql: String, clazz: Class[T], sys: String): List[T] = {
    //获取mysql的connect
    val connection =DriverManager.getConnection(config.getProperty(sys + ".mysql.url"), config.getProperty(sys + ".mysql.user"), config.getProperty(sys + ".mysql.password"))
    //执行sql
    val statement = connection.createStatement()
    val list = new java.util.ArrayList[T]()
    try {
      val resultSet = statement.executeQuery(sql)
      //通过反射获取结果对象值
      while (resultSet.next()) {
        val obj = clazz.newInstance()
        val fields = clazz.getDeclaredFields
        for (field <- fields) {
          field.setAccessible(true)
          field.set(obj, resultSet.getObject(field.getName))
        }
        list.add(obj)
      }
    }catch{
      case e:Exception=>e.printStackTrace()
        println("errorsql:"+sql)
    }finally {
      //关闭连接
      statement.close()
      connection.close()
    }
    list.asScala.toList
  }

  //参考上面的方法 写一个执行sql的方法，并且返回结果状态
  def executeSql(sql: String, sys: String): Boolean = {
    val connection = DriverManager.getConnection(config.getProperty(sys + ".mysql.url"), config.getProperty(sys + ".mysql.user"), config.getProperty(sys + ".mysql.password"))
    val statement = connection.createStatement()
    try {
      statement.execute(sql)
      true
    } catch {
      case e: Exception =>
        e.printStackTrace()
        println("errorsql:" + sql)
        false
    } finally {
      statement.close()
      connection.close()
    }
  }


}
