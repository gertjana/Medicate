package net.addictivesoftware.medicate.rest;

import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.http.auth._
import net.addictivesoftware.medicate.model._
import net.liftweb.common._
import net.liftweb.mapper._
import net.liftweb.util.Helpers.AsLong
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

object MedicateRest extends RestHelper with RestUtils {
  serve {
    case "api" :: "version" :: _ XmlGet _=> {<version>0.1.0</version> }
    case "api" :: "version" :: _ JsonGet _=> { JsonWrapper("version", "0.1.0") }
  }

  // generic query-ing of medicine
  serve( "api" / "medicine" prefix {
    // single medicine
    case AsLong(id) :: _ XmlGet _=>
      {
        Medicine.findByKey(id) match {
          case Full(medicine) => {medicine.asXml}
          case (_) => XmlResponse(errorNode("Medicine with id " + id + " does not exist."), 404, "application/xml")
        }
      }
    case AsLong(id) :: _ JsonGet _=>
      {
        Medicine.findByKey(id) match {
          case Full(medicine) => {medicine.asJson}
          case (_) => XmlResponse(errorNode("Medicine with id " + id + " does not exist."), 404, "application/json")
        }
      }
    // list of medicine
    case Nil XmlGet _=> {
        <Medicines>
          {
            Medicine.findAll.map(medicine => medicine.asXml)
          }
        </Medicines>
      }
    case Nil JsonGet _=> {
        JsonWrapper("medicines", Medicine.findAll.map(medicine => medicine.asJson))
       }
  })

  serve ("api" / "auth" prefix {
    case email :: password :: _ XmlGet _ => {
      User.find(By(User.email, email)) match {
        case Full(u) =>
          if (u.validated && u.password.match_?(password)) {
            <key>{u.uniqueId.is}</key>
          } else {
            XmlResponse(errorNode("failed to authenticate or user is not validated"), 402, "application/xml", Nil);
          }

        case (_) =>
          XmlResponse(errorNode("unknown email"), 402, "application/xml", Nil);

      }
    }
    case user :: pass :: _ JsonGet _ => {
       User.find(By(User.email, user)) match {
        case Full(user) =>
          if (user.validated && user.password.match_?(pass)) {
            JsonWrapper("key", user.uniqueId.is);
          } else {
            JsonResponse(JsonWrapper("error", "failed to authenticate or user is not validated"), Nil, Nil, 402);
          }

        case (_) =>
          JsonResponse(JsonWrapper("error", "unknown email"), Nil, Nil, 402);
      }
    }
  })


  // user specific calls
  serve ( "api" / "user" prefix {
    //return dosages
    case key :: "dosages" :: _ XmlGet _=> {
      var id = getUserIdFromKey(key)
      <Dosages>
        {
          Dose.findAll(By(Dose.user, id)).map(dose => dose.asXml)
        }
      </Dosages>
    }
    case key :: "dosages" :: _ JsonGet _=> {
      var id = getUserIdFromKey(key)
      JsonWrapper("dosages", Dose.findAll(By(Dose.user, id)).map(dose => dose.asJson))
    }

    case key :: "stock" :: _ XmlGet _=> {
      var id = getUserIdFromKey(key)
      <Stocks>
        {
          Stock.findAll(By(Stock.user, id)).map(stock => stock.asXml)
        }
      </Stocks>
    }
    case key :: "stock" :: _ JsonGet _=> {
      var id = getUserIdFromKey(key)
      JsonWrapper("stocks", Stock.findAll(By(Stock.user, id)).map(stock => stock.asJson))
    }

  })

}
