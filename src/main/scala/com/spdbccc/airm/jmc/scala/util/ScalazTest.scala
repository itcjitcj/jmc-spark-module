package com.spdbccc.airm.jmc.scala.util

import scalaz.Scalaz.{ApplicativeIdV, ToValidationOps}
import scalaz.{Lens, ValidationNel}
import scalaz._
import Scalaz._
import scalaz.Leibniz.subst


object ScalazTest {


  /**
   * 示例中，首先定义了一个 Person 类，然后定义了一个验证函数 validateAge，用于验证一个 Person 对象是否满足年龄大于等于 18 的条件。这里使用了 Scalaz 中的 ValidationNel 类型，它可以用来封装可能失败的结果。如果验证成功，则返回封装了 Person 对象的成功结果；如果验证失败，则返回封装了错误信息的失败结果。这里使用了 successNel 和 failureNel 函数来分别构造成功和失败结果。
   *
   * 接下来，定义了一个处理流程 process，它首先对输入的 Person 对象进行验证，然后将验证结果映射为一个字符串，最终返回封装了这个字符串的 ValidationNel 对象。
   */
  def validateTest(): Unit = {
    // 定义一个 Person 类
    case class Person(name: String, age: Int)

    // 定义一个验证函数，验证 Person 的年龄是否大于等于 18
    def validateAge(person: Person): ValidationNel[String, Person] = {
      if (person.age >= 18) person.successNel
      else "Underage person".failureNel
    }

    // 定义一个处理流程
    def process(person: Person): ValidationNel[String, String] = {
      val validatedPerson = validateAge(person)
      val result = validatedPerson.map(p => s"${p.name} is an adult")
      result
    }

    // 测试
    val adult = Person("Alice", 25)
    val underage = Person("Bob", 17)

    println(process(adult)) // 输出：Success(Alice is an adult)

    println(process(underage)) // 输出：Failure(NonEmptyList(Underage person))
  }

  /**
   * 示例中，我们定义了两个 case class，分别代表 Person 和 Order，然后针对它们展示了 Scalaz 中的一些常用功能：
   *
   * Functor：定义了一个 Option 类型的 order 对象，然后使用 map 对它进行映射，提取出其中的 item 字段。
   * Applicative：使用 Applicative 的 <*> 操作符，将一个 Order 对象作为第一个输入，使用 pure 函数将其变为一个 Const 容器，将 amountPerItem 和另一个 Order 对象作为输入函数进行运算，最终得到计算结果。
   * Monoid：使用 foldMap 操作符对包含多个 Order 对象的列表进行折叠，提取出其中的 amount 字段，并使用 Monoid 执行累加运算，得到最终的计算结果。
   * Monad：定义了一个函数 getOrder，输入一个 id，输出一个 Option[Order] 对象，使用 flatMap 从 Option[Order] 中提取出 Order 对象，然后执行操作并输出结果。
   * Validation：定义了一个 underageOrder 对象，其中包含未成年人的订单信息，然后定义了两个验证函数 validatePersonAge 和 validateOrderAmount，使用 applicative 组合两个验证函数，将它们作用于 bob 和 underageOrder 对象上，最终得到结果。
   * Lens：使用 Lens 类型对 Order 类的 id 字段进行更新，得到处理过后的 Order 对象
   */
  def otherCase(): Unit = {
    // 用 case class 定义一个 Person 类
    case class Person(name: String, age: Int, role: String)

    // 用 case class 定义一个 Order 类
    case class Order(id: Int, person: Person, item: String, amount: Int)

    // 定义一些示例数据
    val alice = Person("Alice", 25, "Manager")
    val bob = Person("Bob", 18, "Programmer")
    val order1 = Order(1, alice, "book", 1)
    val order2 = Order(2, bob, "laptop", 5)
    val order3 = Order(2, bob, "phone", 2)

    // 1. Functor 示例
    val optionOrder: Option[Order] = Some(order1)
    val mappedOrder = optionOrder.map(_.item)
    println(s"Functor 示例：${mappedOrder}")

    // 2. Applicative 示例
    val amountPerItem = (o: Order) => o.amount
    val totalPrice = (x: Int, y: Int) => x + y
    //
    //    val result = (Order.apply _).curried <*>(order1.pure[({type L[X] = Const[Int, X]})#L]) <*> amountPerItem <*> (totalPrice.lift[({type L[X] = Const[Int, X]})#L] apply order2)  // 用 <|*|> 也一样，效果相同
    //    println(s"Applicative 示例：${result.getConst}")

    // 3. Monoid 示例
    val totalAmount = List(order1, order2, order3).foldMap(amountPerItem)
    println(s"Monoid 示例：${totalAmount}")

    // 4. Monad 示例
    val getOrder = (id: Int) => if (id == 1) Some(order1) else None
    val orderItem = getOrder(1).flatMap(o => Some(o.item))
    println(s"Monad 示例：${orderItem}")

    // 6. Lens 示例
    val orderLens = Lens.lensu[Order, Int](
      (o, id) => o.copy(id = id),
      (o) => o.id
    )
    val newOrder = orderLens.set(order1, 3)
    println(s"Lens 示例：${newOrder}")

    //
    println(List(1, 2, 3) === List(1, 2, 3))
  }

  def example(): Unit = {
    import std.string._

    assert(charsNel("foo").isJust)
    assert(charsNel("").isEmpty)

    import stringSyntax._

    assert("foo".charsNel.isJust)
    assert("".charsNel.isEmpty)
  }

  //  def wordCount(): Unit = {
  //    import scalaz.{Monoid, StateT}
  //    import scalaz.State
  //
  //    val text = "the cat in the hat\n sat on the mat\n".toList
  //
  //    import scalaz.std.anyVal._, scalaz.std.list._, scalaz.std.boolean.test
  //
  //    // To count words, we need to detect transitions from whitespace to non-whitespace.
  //    // atWordStart_{i} = heaviside( test(isSpace(c_{i}) - test(isSpace(c_{i-1})) )
  //    def atWordStart(c: Char): State[Boolean, Int] = State { (prev: Boolean) =>
  //      val cur = c != ' '
  //      (cur, test(cur && !prev))
  //    }
  //
  //    // To count, we traverse with a function returning 0 or 1, and sum the results
  //    // with Monoid[Int], packaged in a constant monoidal applicative functor.
  //    val Count = Monoid[Int].applicative
  //
  //    // Compose the applicative instance for [a]State[Boolean,a] with the Count applicative
  //    val WordCount = StateT.stateMonad[Boolean].compose[λ[α => Int]](Count)
  //
  //    // Fuse the three applicatives together in parallel...
  //    val A = Count
  //      .product[λ[α => Int]](Count)
  //      .product[λ[α => State[Boolean, Int]]](WordCount)
  //
  //    // ... and execute them in a single traversal
  //    val ((charCount, lineCount), wordCountState) = A.traverse(text)((c: Char) => ((1, test(c == '\n')), atWordStart(c)))
  //    val wordCount = wordCountState.eval(false)
  //
  //    println("%d\t%d\t%d\t".format(lineCount, wordCount, charCount)) // 2	9	35
  //  }
  def arrowUseage(): Unit = {
    val plus1: Int => Int = (_: Int) + 1
    val times2 = (_: Int) * 2
    val rev = (_: String).reverse

    // Function1 arrow

    // Applying first on the Function1 arrow.
    (plus1.first apply (7 -> "abc")) assert_=== (8 -> "abc")

    // Applying second on the Function1 arrow.
    (plus1.second apply ("def" -> 14)) assert_=== ("def" -> 15)

    // Combine plus1 and rev on the Function1 arrow to apply across both elements of a pair.
    (plus1 *** rev apply (7 -> "abc")) assert_=== (8 -> "cba")

    // Perform both plus1 and times2 on a value using the Function1 arrow
    (plus1 &&& times2 apply 7) assert_=== (8 -> 14)

    // Perform plus1 on a pair using the Function1 arrow
    (plus1.product apply (9 -> 99)) assert_=== (10 -> 100)

  }

  def ApplyUsage(): Unit = {
    import scalaz.Apply
    import scalaz.std.option._
    import scalaz.std.list._
    import scalaz.std.string._
    import scalaz.std.anyVal._
    import scalaz.std.vector._
    import scalaz.std.tuple._
    import scalaz.syntax.equal._
    import scalaz.syntax.std.option._

    // Apply extends the (hopefully familiar) Functor Typeclass by adding
    // a method named "ap" which is similar to "map" from Functor in
    // that it applies a function to values in a context, however with
    // ap, the function is also in the same context. Here are some
    // examples, contrasted with map

    val intToString: Int => String = _.toString
    val double: Int => Int = _ * 2
    val addTwo: Int => Int = _ + 2

    // map
    assert(Apply[Option].map(1.some)(intToString) === "1".some)
    assert(Apply[Option].map(1.some)(double) === 2.some)
    assert(Apply[Option].map(none)(double) === none)

    // ap
    assert(Apply[Option].ap(1.some)(some(intToString)) === "1".some)
    assert(Apply[Option].ap(1.some)(some(double)) === 2.some)
    assert(Apply[Option].ap(none)(some(double)) === none)
    assert(Apply[Option].ap(1.some)(none[Int => Int]) === none[Int])
    assert(Apply[Option].ap(none)(none[Int => Int]) === none[Int])
    assert(Apply[List].ap(List(1,2,3))(List(double, addTwo)) === List(2,4,6,3,4,5))

    // from these two methods (map and ap) we are able to derive some
    // very useful methods which allow us to "lift" a function of
    // multiple arguments into a context. There are methods named
    // by how many parameters they take
    val add2 = ((_:Int) + (_:Int))
    val add3 = ((_:Int) + (_:Int) + (_:Int))
    val add4 = ((_:Int) + (_:Int) + (_:Int) + (_:Int))
    assert(Apply[Option].apply2(some(1), some(2))(add2) === some(3))
    assert(Apply[Option].apply3(some(1), some(2), some(3))(add3) === some(6))
    assert(Apply[Option].apply4(some(1), some(2), some(3), some(4))(add4) === some(10))

    // the effects from the context we are operating on are carried
    // through the computation, so, for example, in the case of the
    // Option Apply instance here, if any of the arguments are None, the
    // result of the entire computation is None:
    assert(Apply[Option].apply3(some(1), none, some(3))(add3) === None)

    // tuple2, tuple3 etc, will construct tuples from values from the
    // provided contexts
    assert(Apply[List].tuple3(List(1,2,3), List("a", "b"), List(())) ===
      List((1,"a",()),(1,"b",()),(2,"a",()),
        (2,"b",()),(3,"a",()),(3,"b",())))


    // There some helpful syntax available for the Apply typeclass:
    import scalaz.syntax.apply._

    // <*> is syntax for the "ap" method
    val plus1: Int => Int = _ + 1
    val plus2: Int => Int = _ + 2
    assert(List(1,2,3) <*> List(plus1, plus2) === List(2,3,4,3,4,5))

    // |@| is referred to as "applicative builder", it allows you to
    // evaluate a function of multiple arguments in a context, similar
    // to apply2, apply3, apply4, etc:
    assert((some(1) |@| some(2) |@| some(3))(_ + _ + _) === Some(6))
    assert((some(1) |@| none[Int] |@| some(3))(_ + _ + _) === None)

    def add8(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int, g: Int, h: Int) = a+b+c+d+e+f+g+h
    val someOf8Options = (1.some |@| 2.some |@| 3.some |@| 4.some |@|
      5.some |@| 6.some |@| 7.some |@| 8.some)(add8 _)
    assert(someOf8Options === 36.some)

    // the applicative builder created by |@| also has a "tupled" method
    // which will tuple the arguments
    assert((List(1,2,3) |@| List("a","b","c")).tupled ===
      List(1 -> "a", 1 -> "b", 1 -> "c",
        2 -> "a", 2 -> "b", 2 -> "c",
        3 -> "a", 3 -> "b", 3 -> "c"))


    // there are ^, ^^, ^^^, etc methods which correspond respectively
    // to apply2, apply, apply4, etc.
    assert(^(1.some, 2.some)(_ + _) === 3.some)
    assert(^^^(1.some, 2.some, 3.some, 4.some)(_ + _ + _ + _) === 10.some)
    assert(^^^(1.some, 2.some, none[Int], 4.some)(_ + _ + _ + _) === none)

    // sometimes we will want to apply a function in a context in order
    // to preserve the effects of running the function in a context, but
    // are not interested in the result of the function, for this there
    // are two combinators, *> which discards the value on the left, and
    // <* which discards the value on the right.

    // as an example we'll use "Writer", which a context which performs
    // a computation while accumulating a "side-value". Very commonly,
    // writer is used to emit log messages along with the value being
    // computed.

    import scalaz.{Writer,DList}
    import scalaz.syntax.writer._
    type Logged[A] = Writer[DList[String], A]

    // log a message, return no results (hence Unit)
    def log(message: String): Logged[Unit] = DList(message).tell

    // log that we are adding, and return the results of adding x and y
    def compute(x: Int, y: Int): Logged[Int] =
      log("adding " + x + " and " + y) as (x+y)

    // we log a message "begin", we add two numbers, we log "end",
    // neither calls to "log" compute a value, they are only evaluated
    // for the "effect" of logging a message, so we can use *> and <* to
    // discard the computed value (which in this case is Unit), while
    // preserving the value computed in the call to "compute"
    def addAndLog(x: Int, y: Int): Logged[Int] =
      log("begin") *> compute(x,y) <* log("end")

    val (written,result) = addAndLog(1,2).run
    assert(written === DList("begin", "adding 1 and 2", "end"))
    assert(result === 3)

    val (written2,result2) = addAndLog(1, 10).run
    assert(written2 === DList("begin", "adding 1 and 10", "end"))
    assert(result2 === 11)

    // Apply instances can be composed, which allows us to lift a
    // function into a computation in multiple nested contexts, while
    // applying the effects of all contexts:
    val applyVLO = Apply[Vector] compose Apply[List] compose Apply[Option]

    val deepResult =
      applyVLO.apply2(Vector(List(1.some, none[Int]),
        List(2.some, 3.some)),
        Vector(List("a".some, "b".some, "c".some)))(_.toString + _)

    val expectedDeep = Vector(List(Some("1a"), Some("1b"), Some("1c"),
      None, None, None),
      List(Some("2a"), Some("2b"), Some("2c"),
        Some("3a"), Some("3b"), Some("3c")))

    assert(deepResult === expectedDeep)
  }

  def main(args: Array[String]): Unit = {
    ApplyUsage()
  }
}
