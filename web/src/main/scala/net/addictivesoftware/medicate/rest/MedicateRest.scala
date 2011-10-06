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
      case "api" :: _ XmlGet _ =>


        // single book
        case "api" :: key :: "medicine" :: AsLong(id) :: _ XmlGet _=>
            {
              Medicine.findByKey(id) match {
                case Full(medicine) => {medicine.toXml}
                case (_) => NotFoundResponse("Medicine with id " + id + " not found\r\n")
              }
            }
        // list of medicine
        case "api" :: key :: "medicine" :: "all" :: _ XmlGet _=>
          {
            <Medicine>
              {
                Medicine.findAll.map(medicine => medicine.toXml)
              }
            </Medicine>
          }
    }
}
