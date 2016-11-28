package com.latamautos

/**
  * Created by Harold on 23/11/16.
  */
import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Route
import com.latamautos.resources.QuestionResource
import com.latamautos.services.QuestionService

trait RestInterface extends Resources {

  implicit def executionContext: ExecutionContext

  lazy val questionService = new QuestionService

  val routes: Route = questionRoutes

}

trait Resources extends QuestionResource
