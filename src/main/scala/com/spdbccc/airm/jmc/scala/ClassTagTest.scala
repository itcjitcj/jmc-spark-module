package com.spdbccc.airm.jmc.scala

import scala.reflect.ClassTag

class ClassTagTest [K: ClassTag](){

  //在这个例子中，T和U都可以是任何类型。当您在函数中调用arrayConversion时，Scala编译器会自动为T和U提供ClassTag上下文。
  //
  //示例使用了asInstanceOf，可以安全地从T转换为U，因为ClassTag已经足够提供了类型信息，防止了类型擦除问题。
  def arrayConversion[T: ClassTag, U: ClassTag](input: Array[T]): Array[U] = {
    val output = new Array[U](input.length)
    for (i <- input.indices) {
      output(i) = input(i).asInstanceOf[U]
    }
    output
  }

  //在这个例子中，ClassTag 由上下文中的类型推断获得，而不需要在调用函数时显式地提供它。在函数内部，我们使用 new Array[T](size) 创建一个具有 size 个元素的数组，此时，我们不知道 T 的类型，因此不能将非 null 值赋给 Array[T]。因此我们用 null 进行补充。
  //
  //然而 null 的类型是 Null，不能隐式地转换成 T 类型，此时就需要使用 <ClassTag>.asInstanceOf 进行显式的类型转换了。
  //
  //在这个例子中，ClassTag 允许我们创建一个新的空数组并对其进行操作，而无需在创建新的 Array 实例时了解 T 的确切类型，扩展了代码的灵活性。
  def createEmptyArray[T: ClassTag](size: Int): Array[T] = {
    val array = new Array[T](size)
    for (i <- 0 until size) {
      array(i) = null.asInstanceOf[T]
    }
    array
  }

}
object ClassTagTest {
  def main(args: Array[String]): Unit = {
    val testClass = new ClassTagTest[String]()
    val array = Array("1","2","3")
    val array2 = testClass.arrayConversion[String, String](array)
    println(array2.mkString(","))
    val array3 = testClass.createEmptyArray[Int](3)
    println(array3.mkString(","))
  }
}