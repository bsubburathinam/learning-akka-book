import akka.actor.{Actor, Status}

class ScalaPongActor extends Actor {
  override def receive : Receive = {
    case "Ping" => {
      sender() ! "Pong" // executes in "akka dispatcher..." thread
    }
    case _ => sender() ! Status.Failure(new Exception("unknown message"))
  }
}
