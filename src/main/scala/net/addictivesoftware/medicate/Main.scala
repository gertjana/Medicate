package net.addictivesoftware.medicate


import akka.actor.{ActorSystem, Props}
import spray.can.Http
import akka.io.IO
import net.addictivesotware.flow.MedicateService
import org.slf4j.{Logger, LoggerFactory}

object Main extends App {
  val log = LoggerFactory.getLogger(this.getClass())

  val applicationHost:String = MedicateProperties.getEnvOrProp("OPENSHIFT_INTERNAL_IP")
  val applicationPort:Int    = MedicateProperties.getEnvOrProp("OPENSHIFT_INTERNAL_PORT") toInt

  log.info(f"Server starting on $applicationHost%s:$applicationPort%s")
  implicit val system = ActorSystem()

  val handler = system.actorOf(Props[MedicateService], name = "handler")

  IO(Http) ! Http.Bind(handler, interface = applicationHost, port = applicationPort)

}
