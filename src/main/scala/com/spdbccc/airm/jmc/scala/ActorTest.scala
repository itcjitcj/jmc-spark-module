package com.spdbccc.airm.jmc.scala

import akka.actor.TypedActor.dispatcher

object ActorTest {

  def main(args: Array[String]): Unit = {
    //    actorTest()
    actorTest002()
  }

  //这个示例演示了如何使用 Akka Actors 来实现消息传递。在这个例子中，我们定义了一个简单的 SimpleActor 类，它定义了一个 receive 方法，该方法用于接收和处理消息。然后，我们启动一个名为 SimpleActorSystem 的 Actor 系统，并在其中创建一个名为 simpleActor 的 Actor 实例。最后，我们向 simpleActor 发送一条名为 Message 的消息，该消息包含发送者、接收者和消息文本。
  def actorTest(): Unit = {
    import akka.actor.{Actor, ActorSystem, Props}

    case class Message(from: String, to: String, text: String)

    class SimpleActor extends Actor {
      def receive: Receive = {
        case Message(from, to, text) => {
          println(s"Received message: $text")
        }
      }
    }

    val system = ActorSystem("SimpleActorSystem")
    val actor = system.actorOf(Props[SimpleActor], "simpleActor")

    actor ! Message("Alice", "Bob", "Hello, Bob!")


    system.terminate()
  }

  //这个示例演示了如何使用 Akka Streams 来实现响应式编程。在这个例子中，我们创建了一个名为 numbers 的流，其中包含从 1 到 100 的整数。然后，我们使用 map 方法将每个整数乘以 2，创建一个新的流。最后，我们使用 runWith 方法将集合成一个结果，并使用 Sink.fold 方法将结果累加起来。
  def actorTest002(): Unit = {
    import akka.actor.ActorSystem
    import akka.stream.ActorMaterializer
    import akka.stream.scaladsl.{Sink, Source}

    val system = ActorSystem("SimpleStreamExample")
    implicit val materializer: ActorMaterializer = ActorMaterializer()(system)
    //使用 Akka Streams 来实现响应式编程
    val factorials = Source(1 to 10).scan(BigInt(1))((acc, next) => acc * next)
    factorials.runWith(Sink.foreach(println))

    system.terminate()
  }

}
