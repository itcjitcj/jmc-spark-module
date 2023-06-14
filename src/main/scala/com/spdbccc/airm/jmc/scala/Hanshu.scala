package com.spdbccc.airm.jmc.scala

object Hanshu {

  def main(args: Array[String]): Unit = {


    //将下面代码抽取成通用方法
    val numbers = List(1, 2, 3, 4, 5)
    println(evenSquares(numbers))  // 输出 List(4, 16)

    val square = (x: Int) => x * x
    val addOne = (x: Int) => x + 1

    val squareAndAddOne = compose(addOne, square)

    println(squareAndAddOne(3)) // 输出 10

    //使用高阶函数来实现柯里化：
    val addTwo = add(2) _
    println(addTwo(3))
  }

  //使用 for 推导式来过滤和转换列表
  def evenSquares(numbers: List[Int]): List[Int] = {
    for {
      n <- numbers
      if n % 2 == 0
    } yield n * n
  }

  def compose[A, B, C](f: B => C, g: A => B): A => C = { x: A =>
    f(g(x))
  }

  def add(a: Int)(b: Int): Int = a + b

  //省略函数名
  def ignoreFun(): Unit = {
    val add = (x: Int, y: Int) => x + y
    val sum = add(10, 20) // sum = 30
    println(sum)
  }
  //变长参数
  def bianchang(): Unit = {
    def sum(nums: Int*) = nums.sum

    val result1 = sum(1, 2, 3) // result1 = 6
    val result2 = sum(1 to 100: _*) // result2 = 5050
  }


}
