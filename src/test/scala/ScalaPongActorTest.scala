import java.io.{PrintWriter, StringWriter}

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import org.scalatest.{FunSpecLike, Matchers}
import akka.pattern.ask

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ScalaPongActorTest extends FunSpecLike with Matchers
{
  val system = ActorSystem()

  implicit val timeout = Timeout(5 seconds)
  val pongActor = system.actorOf(Props(classOf[ScalaPongActor]))

  describe("Pong actor") { // The subject of the test
    import scala.concurrent.ExecutionContext.Implicits.global
    it ("should respond with Pong") { // Behavior expected from subject
      val future = askPong("Ping").flatMap(_ => askPong("Ping"))
      val result = Await.result(future.mapTo[String], 1 second)
      assert( "Pong" === result )
   }
    it ("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }

  private def askPong(msg:String) : Future[String] = {
    (this.pongActor ? msg).mapTo[String]
  }
}
