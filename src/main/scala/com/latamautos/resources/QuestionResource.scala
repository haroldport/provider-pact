package com.latamautos.resources

/**
  * Created by Harold on 23/11/16.
  */

import javax.ws.rs.{Path, PathParam}

import akka.http.scaladsl.server.{Route, RouteConcatenation}
import com.latamautos.BootedCore
import com.latamautos.entities.{Question, QuestionUpdate}
import com.latamautos.routing.MyResource
import com.latamautos.services.QuestionService
import com.latamautos.swagger.SwaggerDocService
import io.swagger.annotations._

@Api(value = "Question CRUD")
@Path(value = "questions2")
trait QuestionResource extends MyResource with RouteConcatenation with CorsSupport with BootedCore {

  private implicit val _ = system.dispatcher

  val questionService: QuestionService

  def questionRoutes: Route = pathPrefix("questions2") {
    postRoute ~
      path(Segment) { id =>
        getRoute(id) ~
          putRoute(id) ~
          deleteRoute(id)
      }
  } ~
    corsHandler(new SwaggerDocService(system).routes)

  @ApiOperation(value = "Post Question", notes = "Post question", nickname = "anonymousQuestion", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", required = true, dataType = "com.latamautos.entities.Question", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Return Id", response = classOf[String]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def postRoute: Route = pathEnd {
    post {
      entity(as[Question]) { question =>
        completeWithLocationHeader(
          resourceId = questionService.createQuestion(question),
          ifDefinedStatus = 201, ifEmptyStatus = 409)
      }
    }
  }

  @Path("/{id}")
  @ApiOperation(value = "Return Question", notes = "Get question by id", nickname = "anonymousQuestion", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Return Question", response = classOf[Question]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getRoute(@PathParam("id") id: String): Route = get {
    complete(questionService.getQuestion(id))
  }

  def putRoute(id: String): Route = put {
    entity(as[QuestionUpdate]) { update =>
      complete(questionService.updateQuestion(id, update))
    }
  }

  def deleteRoute(id: String): Route = delete {
    complete(questionService.deleteQuestion(id))
  }

}
