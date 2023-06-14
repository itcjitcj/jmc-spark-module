package com.spdbccc.airm.jmc.scala
import scala.collection.{immutable, mutable}

object CollectionTest {

  def main(args: Array[String]): Unit = {
    testMap()
  }

  //list的操作
  def testList(): Unit = {
    val list = List(1, 2, 3, 4, 5)
    val list2 = list.map(_ * 2)
    println(list2)
    val list3 = list.map(x => x * 2)
    println(list3)
    val list4 = list.map(x => x * x)
    println(list4)
    val list5 = list.map(x => "hello" + x)
    println(list5)
    val list6 = list.map(x => List(x, x * 2))
    println(list6)
    val list7 = list.flatMap(x => List(x, x * 2))
    println(list7)
    val list8 = list.filter(x => x % 2 == 0)
    println(list8)
    val list9 = list.filter(x => x % 2 != 0)
    println(list9)
    val list10 = list.filter(x => x % 2 != 0).map(x => x * x)
    println(list10)
    val list11 = list.filter(x => x % 2 != 0).map(x => x * x).sum
    println(list11)
    val list12 = list.filter(x => x % 2 != 0).map(x => x * x).reduce((x, y) => x + y)
    println(list12)
    val list13 = list.filter(x => x % 2 != 0).map(x => x * x).reduce(_ + _)
    println(list13)
    val list14 = list.filter(x => x % 2 != 0).map(x => x * x).reduce(_ * _)
    println(list14)
    val list15 = list.filter(x => x % 2 != 0).map(x => x * x).fold(0)(_ + _)
    println(list15)
    val list16 = list.filter(x => x % 2 != 0).map(x => x * x).fold(1)(_ * _)
    println(list16)
    val list17 = list.filter(x => x % 2 != 0).map(x => x * x).foldLeft(0)(_ + _)
    println(list17)
    val list18 = list.filter(x => x % 2 != 0).map(x => x * x).foldLeft(1)(_ * _)
    println(list18)
  }

  //list的一些增删改查
  def testList2(): Unit = {
    val list = List(1, 2, 3, 4, 5)
    val list2 = list :+ 6
    println(list2)
    val list3 = 0 +: list
    println(list3)
    val list4 = list.updated(0, 0)
    println(list4)
    //ArrayBuffer 的增删改查 相当于java中的ArrayList
    //ArrayBuffer 的底层数据结构是基于数组的，由于数组在内存中是连续分布的，因此ArrayBuffer 的性能比ListBuffer 更高，尤其在插入和删除元素时。
    val list5 = mutable.ArrayBuffer(1, 2, 3, 4, 5)
    list5 += 6
    println("list5:"+list5)
    list5 -= 1
    println("list5:"+list5)
    list5 ++= List(6, 7, 8)
    println("list5:"+list5)
    list5 --= List(1, 2, 3)
    println("list5:"+list5)
    list5(0) = 0
    println("list5:"+list5)
    list5.update(0, 0)
    println("list5:"+list5)
    list5.insert(0, 0)
    println("list5:"+list5)
    list5.remove(0)
    println("list5:"+list5)
    //listBuffer的增删改查 相当于java中的LinkedList
    // ListBuffer 的底层数据结构是链表，因此它在插入和删除元素时的性能比 ArrayBuffer 更高，尤其在头部和尾部插入元素的情况下，因为链表可以通过修改指针来实现插入和删除操作。
    val list6 = mutable.ListBuffer(1, 2, 3, 4, 5)
    list6 += 6
    println("list6:"+list6)
    list6 -= 1
    println("list6:"+list6)
    list6 ++= List(6, 7, 8)
    println("list6:"+list6)
    list6 --= List(1, 2, 3)
    println("list6:"+list6)
    list6(0) = 0
    println("list6:"+list6)
    list6.update(0, 0)
    println("list6:"+list6)
  }

  //set的一些增删改查
  def testSet(): Unit = {
    val set = Set(1, 2, 3, 4, 5)
    val set2 = set + 6
    println("set2:"+set2)
    val set3 = set - 1
    println("set3:"+set3)
    val set4 = set ++ Set(6, 7, 8)
    println("set4:"+set4)
    val set5 = set -- Set(1, 2, 3)
    println("set5:"+set5)
    val set6 = set & Set(1, 2, 3)
    println("set6:"+set6)
    val set7 = set | Set(1, 2, 3)
    println("set7:"+set7)
    val set8 = set &~ Set(1, 2, 3)
    println("set8:"+set8)
    val set9 = set.intersect(Set(1, 2, 3))
    println("set9:"+set9)
    val set10 = set.union(Set(1, 2, 3))
    println("set10:"+set10)
    val set11 = set.diff(Set(1, 2, 3))
    println("set11:"+set11)
    val set12 = set.max
    println("set12:"+set12)
    val set13 = set.min
    println("set13:"+set13)
    val set14 = set.sum
    println("set14:"+set14)
    val set15 = set.product
    println("set15:"+set15)
    val set16 = set.size
    println("set16:"+set16)
    val set17 = set.isEmpty
    println("set17:"+set17)
    val set18 = set.contains(1)
    println("set18:"+set18)
    val set19 = set.exists(_ > 3)
    println("set19:"+set19)
    val set20 = set.forall(_ > 3)
    println("set20:"+set20)
    val set21 = set.find(_ > 3)
    println("set21:"+set21)
    val set22 = set.find(_ > 3).getOrElse(0)
    println("set22:"+set22)
    val set24 = set.map(_ * 2)
    println("set24:"+set24)
    val set25 = set.flatMap(x => Set(x, x * 2))
    println("set25:"+set25)
    val set26: List[Int] = set.toList
    println("set26:"+set26)
    val set27: Array[Int] = set.toArray
    println("set27:"+set27.mkString("Array(", ", ", ")"))
    val set28: mutable.Buffer[Int] = set.toBuffer
    println("set28:"+set28)
    val set29: Seq[Int] = set.toSeq
    println("set29:"+set29)
    val set30: immutable.IndexedSeq[Int] = set.toIndexedSeq
    println("set30:"+set30)
  }

  //array的一些增删改查
  def testArray(): Unit = {
    val array = Array(1, 2, 3, 4, 5)
    val array2 = array :+ 6
    println("array2:"+array2.mkString(","))
    val array3 = 0 +: array
    println("array3:"+array3.mkString(","))
    val array4 = array.updated(0, 0)
    println("array4:"+array4.mkString(","))
    val array5 = array ++ Array(6, 7, 8)
    println("array5:"+array5.mkString(","))
    val array22: mutable.Buffer[Int] = array.toBuffer
    println("array22:" + array22)
    val array6 = array.toList
    println("array6:"+array6)
  }
  //map的一些增删改查
  def testMap(): Unit = {
    val map = Map("a" -> 1, "b" -> 2, "c" -> 3)
    val map2 = map + ("d" -> 4)
    println("map2:" + map2)
    val map3 = map - "a"
    println("map3:" + map3)
    val map4 = map ++ Map("d" -> 4, "e" -> 5)
    println("map4:" + map4)
    val map5 = map -- List("a", "b", "c")
    println("map5:" + map5)
    val map6 = map.updated("a", 0)
    println("map6:" + map6)
    //可变map的一些增删改查
    val map7 = mutable.Map("a" -> 1, "b" -> 2, "c" -> 3)
    map7 += ("d" -> 4)
    println("map7:" + map7)
    map7 -= "a"
    println("map7:" + map7)
    map7 ++= Map("d" -> 4, "e" -> 5)
    println("map7:" + map7)
    map7 --= List("a", "b", "c")
    println("map7:" + map7)
    map7("a") = 0
    println("map7:" + map7)
    map7.update("a", 0)
    println("map7:" + map7)
  }

}
