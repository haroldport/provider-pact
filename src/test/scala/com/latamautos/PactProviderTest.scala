package com.latamautos

/**
  * Created by Harold on 25/11/16.
  */
import java.io.File
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import com.atlassian.oai.validator.pact.PactProviderValidator
import org.scalatest._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.latamautos.resources.CorsSupport
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.concurrent.duration._

class PactProviderTest extends FunSuiteLike with CorsSupport with RestInterface {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit override lazy val system = ActorSystem("quiz-management-service")

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  def routeSwagger = routes

  Http().bindAndHandle(routeSwagger, host, port) map { binding =>
    println(s"REST interface bound to ${binding.localAddress}") } recover { case ex =>
    println(s"REST interface could not bind to $host:$port", ex.getMessage)
  }

  def stopWebServer(bindingFuture: Future[ServerBinding]): Unit = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => {
        system.terminate
      })
  }

  val SWAGGER_URL = "http://localhost:5000/api-docs/swagger.json"
  val pactDir = "/Users/Harold/projects/provider-pact/src/main/resources/pacts"

  test("validateCmd WHEN messageId is empty SHOULD return (None, None)") {
    val validator: PactProviderValidator = PactProviderValidator.createFor(SWAGGER_URL).withConsumer("ExampleConsumer", gelLastPactFile.get.getAbsolutePath).build
    println("======================validator.validate() = " + validator.validate().hasErrors)
    println("======================validator.validate() = " + validator.validate().getValidationFailureReport)
    assert(!validator.validate().hasErrors)
  }

  def gelLastPactFile:Option[File] = {
    val file:File = new File(pactDir)
    file.listFiles().toList
      .map(file => Paths.get(file.getAbsolutePath))
      .map(path => (Files.readAttributes(path, classOf[BasicFileAttributes]), path.toFile))
      .sortWith((a, b) => a._1.creationTime().toMillis > b._1.creationTime().toMillis)
      .map(a => a._2.getAbsoluteFile) match {
      case file::files => Some(file)
      case Nil => None
    }
  }


}
