enablePlugins(JavaServerAppPackaging)

name := "provider-pact"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.bintrayRepo("hseeberger", "maven"))

libraryDependencies ++= {
  val akkaVersion = "2.4.12"
  val Json4sVersion = "3.2.11"
  val swaggerRequestValidatorVersion = "1.0.6"
  val http4sVersion = "0.14.8a"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % "10.0.0",
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" %  "logback-classic" % "1.1.2",
    "org.json4s" %% "json4s-native" % Json4sVersion,
    "org.json4s" %% "json4s-ext" % Json4sVersion,
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.7.2",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.4",
    "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.2",
    "de.heikoseeberger" %% "akka-http-json4s" % "1.4.2",
    "com.atlassian.oai" % "swagger-request-validator-pact" % swaggerRequestValidatorVersion % "test",
    "au.com.dius" % "pact-jvm-provider_2.11" % "3.2.6" % "test",
    "org.scalatest"  %% "scalatest"           % "2.2.1" % "test",
    "com.itv"        %% "scalapact-scalatest" % "2.0.0" % "test",
    "com.amazonaws" % "aws-java-sdk" % "1.11.60" % "test"
  )
}

import au.com.dius.pact.provider.sbt._
SbtProviderPlugin.config ++ Seq(
  providers := Seq(
    ProviderConfig(name = "Our Service")
      .hasPactWith(ConsumerConfig(name = "consumer", pactFile = file("src/test/resources/Consumer.json")))
  )
)

    