package com.spdbccc.airm.jmc.scala

object YiBuTest {


  def main(args: Array[String]): Unit = {
    test001()
  }

  //这个示例演示了如何使用 Futures 和 map 函数来实现异步编程。在这个例子中，我们定义了一个 add 函数，它将两个整数相加，并返回一个 Future 对象，该对象在后台计算结果。然后，我们使用 map 方法将结果乘以 2，创建一个新的 Future 对象。最后，我们使用 foreach 方法处理结果，并将其打印出来。
  def test001(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future

    def add(a: Int, b: Int): Future[Int] = Future {
      Thread.sleep(1000)
      a + b
    }

    val result = add(2, 3).map(_ * 2)

    result.foreach(println) // 输出 10

    println(123)
    Thread.sleep(2000)
  }

  def test002(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future
    import scala.util.{Success, Failure}

    def add(a: Int, b: Int): Future[Int] = Future {
      Thread.sleep(1000)
      a + b
    }

    val result = add(2, 3)
    result.onComplete {
      case Success(value) => println(s"Result: $value")
      case Failure(exception) => println(s"Exception: ${exception.getMessage}")
    }
    println(123)
    Thread.sleep(2000)
  }

}
