package com.latamautos

import akka.actor.ActorSystem

/**
  * Created by Harold on 23/11/16.
  */
trait Core {

  implicit def system: ActorSystem

}

trait BootedCore extends Core {

  /**
    * Construct the ActorSystem we will use in our application
    */
  implicit lazy val system = ActorSystem("quiz-management-service")

  /**
    * Ensure that the constructed ActorSystem is shut down when the JVM shuts down
    */
  sys.addShutdownHook(system.terminate())

}
