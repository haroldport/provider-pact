package com.latamautos
import java.util.concurrent.Executors

import org.http4s.dsl._
import org.http4s.websocket.WebsocketBits._

import org.http4s.HttpService
import org.http4s.server.staticcontent
import org.http4s.server.staticcontent.ResourceService.Config
import org.http4s.server.websocket.WS


import scala.concurrent.duration._

import scalaz.stream.{Exchange, Process, time}
import scalaz.stream.async.topic

class Routes {
  private implicit val scheduledEC = Executors.newScheduledThreadPool(4)

  // Provides the message board for our websocket chat
  private val chatTopic = topic[String]()

  // Get the static content
  private val static  = cachedResource(Config("/static", "/static"))
  private val views   = cachedResource(Config("/staticviews", "/"))
  private val swagger = cachedResource(Config("/swagger", "/swagger"))

  val service: HttpService = HttpService {

    /** Working with websockets is simple with http4s */

    case r @ GET -> Root / "hello" =>

      Ok("Hello, better world.")
  }

  private def cachedResource(config: Config): HttpService = {
    val cachedConfig = config.copy(cacheStartegy = staticcontent.MemoryCache())
    staticcontent.resourceService(cachedConfig)
  }
}