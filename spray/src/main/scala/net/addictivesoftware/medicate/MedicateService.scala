package net.addictivesotware.flow

import akka.actor.Actor
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport
import spray.http.MediaTypes.{`text/html`, `text/plain`, `application/json`}
import net.addictivesoftware.medicate.WebPages
import net.addictivesoftware.medicate.objects.Medicine

//implicit marshallers/unmarshallers
import spray.json.DefaultJsonProtocol._
import net.addictivesoftware.medicate.MyJsonProtocol._


/**
 * Actor that recieves requests and executes the mixed-in route
 */
class MedicateService extends Actor with MedicateRoutingService {
  def actorRefFactory = context
  def receive = runRoute(medicateRoute)
}


/**
 * Trait that contains the route to execute
 */
trait MedicateRoutingService extends HttpService with WebPages with SprayJsonSupport {
  val Ok = "Ok"

  val medicateRoute = pathPrefix("api") {
    path("medicine") {
      get {
        respondWithMediaType(`application/json`) {
          complete {
            Medicine.list()
          }
        }
      }
    } ~
    path("medicine" / "\\w+".r) {id =>
      get {
        respondWithMediaType(`application/json`) {
          complete {
            Medicine.getById(id)
          }
        }
      }
    } ~
    path("version") {
      get {
        respondWithMediaType(`text/plain`) {
          complete {
            "1.0"
          }
        }
      }
    }
  }
}
