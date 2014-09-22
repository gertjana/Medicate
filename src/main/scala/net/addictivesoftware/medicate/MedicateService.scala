package net.addictivesotware.flow

import akka.actor.Actor
import spray.routing.HttpService
import spray.httpx.SprayJsonSupport
import spray.http.MediaTypes.{`text/plain`, `application/json`}
import net.addictivesoftware.medicate.{Result, MyJsonProtocol, WebPages}
import net.addictivesoftware.medicate.objects.{MedicineObject, Medicine}



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
trait MedicateRoutingService extends HttpService with WebPages with SprayJsonSupport with MyJsonProtocol {
  val Ok = "Ok"
  val NoResults = "No Results found"

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
    path("medicine" / "\\d+".r) {id =>
      get {
        respondWithMediaType(`application/json`) {
          complete {
             Medicine.getByNr(id.toInt) match {
               case Some(medicine) => medicine
               case _ => NoResults
             }
          }
        }
      } ~
      post {
        entity(as[MedicineObject]) { medicine =>
          complete {
            Medicine.insert(medicine)
          }
        }
      } ~
      put {
        entity(as[MedicineObject]) { medicine =>
          complete {
            Result(Medicine.update(id, medicine).toString)
          }
        }
      } ~
      delete {
        complete {
          Result(Medicine.deleteById(id).toString)
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
