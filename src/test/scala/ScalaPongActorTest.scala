import java.io.{PrintWriter, StringWriter}

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import org.scalatest.{FunSpecLike, Matchers}
import akka.pattern.ask

import scala.concurrent.Await
import scala.concurrent.duration._

class ScalaPongActorTest extends FunSpecLike with Matchers
{
  val system = ActorSystem()

  implicit val timeout = Timeout(5 seconds)
  val pongActor = system.actorOf(Props(classOf[ScalaPongActor]))

  describe("Pong actor") { // The subject of the test
    import scala.concurrent.ExecutionContext.Implicits.global
    it ("should respond with Pong") { // Behavior expected from subject
      val future = pongActor ? "Ping"
      future
        .onSuccess({
          case "Pong" => { // executes in "Fork join pool" thread
            println("Got pong.")
          }
          case _ => throw new IllegalStateException("Did not receive Pong.")
        })
      Thread.sleep(1000)
   }
    it ("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }
}
