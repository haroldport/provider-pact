package com.latamautos

/**
  * Created by Harold on 25/11/16.
  */
import java.io.File
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Paths}

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import com.atlassian.oai.validator.pact.PactProviderValidator
import org.scalatest._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.latamautos.resources.CorsSupport
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class PactProviderTest extends FunSuiteLike with CorsSupport with BootedCore with RestInterface {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val routeSwagger = routes

  def startWebServer(): Future[ServerBinding] = {
    val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routeSwagger, host, port)
    bindingFuture
  }

  def stopWebServer(bindingFuture: Future[ServerBinding]): Unit = {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => {
        system.terminate
      })
  }

  val SWAGGER_URL = "http://localhost:5000/api-docs/swagger.json"
  val pactDir = "/Users/Harold/projects/provider-pact/target/pacts"

  test("validateCmd WHEN messageId is empty SHOULD return (None, None)") {
    val bindingFuture: Future[ServerBinding] = startWebServer()
    Await.ready(bindingFuture, Duration.Inf)
    println(s"--bindingFuture-->>>> $bindingFuture")
    val validator: PactProviderValidator = PactProviderValidator.createFor(SWAGGER_URL).withConsumer("ExampleConsumer", gelLastPactFile.get.getAbsolutePath).build
    println("======================validator.validate() = " + validator.validate().hasErrors)
    println("======================validato r.validate() = " + validator.validate().getValidationFailureReport)
    assert(!validator.validate().hasErrors)
    stopWebServer(bindingFuture)
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
