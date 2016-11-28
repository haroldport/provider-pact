/**
  * Created by Harold on 23/11/16.
  */
import scala.concurrent.duration._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.latamautos.{BootedCore, RestInterface}
import com.typesafe.config.ConfigFactory

object Main extends App with RestInterface with BootedCore{
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val api = routes

  Http().bindAndHandle(api, host, port) map { binding =>
    println(s"REST interface bound to ${binding.localAddress}") } recover { case ex =>
    println(s"REST interface could not bind to $host:$port", ex.getMessage)
  }
}
