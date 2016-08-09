import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import org.scalatest.{FunSpecLike, Matchers}
import akka.pattern.ask

import scala.concurrent.Await
import scala.concurrent.duration._

class ScalaPongActorTest extends FunSpecLike
{
  val system = ActorSystem()

  implicit val timeout = Timeout(5 seconds)
  val pongActor = system.actorOf(Props(classOf[ScalaPongActor]))

  describe("Pong actor") { // The subject of the test
    it ("should respond with Pong") { // Behavior expected from subject
      val future = pongActor ? "Ping"
      val result = Await.result(future.mapTo[String], 1 second)
      assert( "Pong" ===  result )
    }
    it ("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }
}
