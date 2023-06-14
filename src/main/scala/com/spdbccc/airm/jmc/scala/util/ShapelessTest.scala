package com.spdbccc.airm.jmc.scala.util
import shapeless.HList.ListCompat.::
import shapeless._
import shapeless.ops.product._
import shapeless.record.recordOps
import shapeless.syntax.std.tuple.productTupleOps
import shapeless.syntax.typeable._

object ShapelessTest {
  def main(args: Array[String]): Unit = {

    typeTest()
  }

  //Shapeless 的 HList 是一个常用的数据结构，它可以代表一个元素类型可变的 List。使用 HList 可以进行很多方便的操作
  def testList(): Unit = {
    // 定义一个 HList
    val hlist: Int :: String :: Boolean :: HNil = 1 :: "hello" :: true :: HNil

    // 访问 HList 的元素
    println(hlist.head) // 1
    println(hlist.tail.head) // "hello"

    // 添加元素到 HList
    println(2 :: hlist) // 2 :: 1 :: "hello" :: true :: HNil

    // 转换 HList 的类型
    case class Person(name: String, age: Int)
    val person = "Alice" :: 25 :: HNil
    val caseClass = Generic[Person].from(person)
    println(caseClass) // Person(Alice,25)
  }

  //Shapeless 可以用于提取 case class 的结构和属性，可以方便地进行各种操作
  def caseClass(): Unit = {
    // 定义一个 case class
    case class Person(name: String, age: Int)

    // 访问 case class 的属性
    val person = Person("Alice", 25)
    println(person.productElement(0)) // Alice
    println(person.productArity) // 2

    // 将 case class 转换为 HList
    val hlist = Generic[Person].to(person)
    println(hlist) // Alice :: 25 :: HNil

    // 从 HList 中构建 case class
//    val person2 = Generic[Person].from(hlist.updated(1, 30)) // 修改 age 的值为 30
//    println(person2) // Person(Alice,30)

    // 对 case class 展开应用函数
//    val resultHList = person.foldLeft(HNil: HList)((acc, elem) => elem :: acc)
//    val result = resultHList.reverse // HList to List
//    println(result) // List(25, Alice)
  }

  //Shapeless 还可以方便地操作 Type 类型，比如可以将类型转换为字符串
  def typeTest(): Unit = {
    //Shapeless 的一些type用法
    type T = Int :: String :: Boolean :: HNil
    val t: T = 1 :: "hello" :: true :: HNil

    // 可以将类型转换为字符串
    println(implicitly[Typeable[T]].describe) // Int :: String :: Boolean :: HNil

  }

}
