package com.spdbccc.airm.jmc.scala

import scala.annotation.tailrec
import scala.util.Try
import scala.util.Success
import scala.util.Failure

object TryExceptionTest {
  //这个函数使用了函数式编程的技巧，如闭包、嵌套函数等，
  // 用于在多次尝试后执行一个给定的代码块，直到成功或者尝试次数用尽。
  // 它使用 Try 和 Success / Failure 来处理错误，并使用递归重试最多 times 次。
  def retry[T](times: Int)(block: => T): Try[T] = {
    @tailrec
    def attempt(times: Int): Try[T] = {
      Try {
        block
      } match {
        case Success(result) => Success(result)
        case Failure(e) if times > 1 =>
          println(s"Failed, $times attempts left, retrying in 1 sec.")
          Thread.sleep(1000)
          attempt(times - 1)
        case Failure(e) =>
          println(s"Failed, no more attempts left, giving up. Exception: $e")
          Failure(e)
      }
    }

    attempt(times)
  }


  def main(args: Array[String]): Unit = {
    val result = retry(3) {
      println("Trying...")
      throw new Exception("Something went wrong")
    }
    println(result)
  }
}
