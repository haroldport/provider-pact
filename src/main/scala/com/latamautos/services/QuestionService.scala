package com.latamautos.services

/**
  * Created by Harold on 23/11/16.
  */

import com.latamautos.entities.{Question, QuestionUpdate}

import scala.concurrent.{ExecutionContext, Future}

class QuestionService(implicit val executionContext: ExecutionContext) {

  var questions = Vector.empty[Question]

  def createQuestion(question: Question): Future[Option[String]] = Future {
    questions.find(_.id == question.id) match {
      case Some(q) => None
      case None =>
        questions = questions :+ question
        Some(question.id)
    }
  }

  def getQuestion(id: String): Future[Option[Question]] = Future {
    questions.find(_.id == id)
  }

  def updateQuestion(id: String, update: QuestionUpdate): Future[Option[Question]] = {

    def updateEntity(question: Question): Question = {
      val title = update.title.getOrElse(question.title)
      val text = update.text.getOrElse(question.text)
      Question(id, title, text)
    }

    getQuestion(id).flatMap {
      case None => Future {
        None
      }
      case Some(question) =>
        val updatedQuestion = updateEntity(question)
        deleteQuestion(id).flatMap { _ =>
          createQuestion(updatedQuestion).map(_ => Some(updatedQuestion))
        }
    }
  }

  def deleteQuestion(id: String): Future[Unit] = Future {
    questions = questions.filterNot(_.id == id)
  }


}