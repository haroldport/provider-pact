package com.latamautos

/**
  * Created by Harold on 25/11/16.
  */
package valid

import java.io.File
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{Files, Paths}

import com.atlassian.oai.validator.pact.PactProviderValidator
import org.scalatest._

class PactProviderTest extends FunSuiteLike {

  val SWAGGER_URL = "http://localhost:5000/api-docs/swagger.json"
  val pactDir = "/Users/Harold/projects/provider-pact/target/pacts"

  test("validateCmd WHEN messageId is empty SHOULD return (None, None)") {
    val validator: PactProviderValidator = PactProviderValidator.createFor(SWAGGER_URL).withConsumer("ExampleConsumer", gelLastPactFile.get.getAbsolutePath).build
    println("======================validator.validate() = " + validator.validate().hasErrors)
    println("======================validato r.validate() = " + validator.validate().getValidationFailureReport)
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
