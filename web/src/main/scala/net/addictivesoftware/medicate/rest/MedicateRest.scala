/*
 * Copyright 2006-2011 Addictive Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.addictivesoftware.medicate.rest;

import net.liftweb.http._
import net.liftweb.http.rest._
import net.liftweb.http.auth._
import net.addictivesoftware.medicate.model._
import net.addictivesoftware.medicate.lib.CollectionUtils
import net.liftweb.common._
import net.liftweb.mapper._
import net.liftweb.util.Helpers.AsLong
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

/**
 * Main REST interface
 * 
 * Extended with Liftweb's Resthelper and our own @see RestUtils and @see CollectionUtils
 */
object MedicateRest extends RestHelper with RestUtils with CollectionUtils {
  def version = "1.0";

  // generic query-ing of medicine
  serve( "api" / version / "medicine" prefix {
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
          case (_) => JsonResponse("error" -> "Medicine with id %d does not exist.".format(id), 404)
        }
      }

    // list of medicine
    case Nil XmlGet _=> {
        <Medicines>
          {
            Medicine.findAll(OrderBy(Medicine.name, Ascending)).map(medicine => medicine.asXml)
          }
        </Medicines>
      }

    case Nil JsonGet _=> {
        ("medicines", Medicine.findAll(OrderBy(Medicine.name, Ascending)).map(medicine => medicine.asJson)):JValue
       }
  })

  // user authentication
  // FIXME now has password in the url in plain text
  serve ("api" / version / "auth" prefix {
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
            ("key", user.uniqueId.is):JValue;
          } else {
            JsonResponse(("error", "failed to authenticate or user is not validated"):JValue, Nil, Nil, 402);
          }

        case (_) =>
          JsonResponse(("error", "unknown email"):JValue, Nil, Nil, 402);
      }
    }
  })

  // user specific calls
  serve ( "api" / version / "user" prefix {
    //return dosages
    case key :: "dosages" :: _ XmlGet _=> {
      var id = getUserIdFromKey(key)
      <dosages>
        {
          Dose.findAll(By(Dose.user, id)).map(dose => dose.asXml)
        }
      </dosages>
    }
    case key :: "dosages" :: _ JsonGet _=> {
      var id = getUserIdFromKey(key)
      ("dosages" -> Dose.findAll(By(Dose.user, id)).map(dose => dose.asJson)) : JValue
    }

    // return stock
    case key :: "stock" :: _ XmlGet _=> {
      var id = getUserIdFromKey(key)
      <stocks>
        {
          Stock.findAll(By(Stock.user, id)).map(stock => stock.asXml)
        }
      </stocks>
    }
    case key :: "stock" :: _ JsonGet _=> {
      var id = getUserIdFromKey(key)
      ("stocks", Stock.findAll(By(Stock.user, id)).map(stock => stock.asJson)):JValue
    }

    //return days left for each medicine
    case key :: "supplies" :: _ XmlGet _=> {
      var id = getUserIdFromKey(key)
      val supplies = calculateSupplies(id)

      <supplies>
        {
          supplies.map(kv =>
            <supply>
              <medicine>{kv._1}</medicine>
              <daysLeft>{kv._2}</daysLeft>
            </supply>
          )
        }
      </supplies>

    }

    case key :: "supplies" :: _ JsonGet _=> {
      var id = getUserIdFromKey(key)
      val supplies:Map[String, Long] = calculateSupplies(id)

      ("Supplies" -> {
        supplies.toList.sortBy(_._1).map(kv => {
          ("Supply" ->
            ("Medicine" -> kv._1) ~
            ("DaysLeft" -> kv._2)
          )
        })
      }) : JValue
    }
  })

  /**
   * Helper method that returns the nr of days the user will have medicines with his current intake as a map
   * @param the id of the user
   * @return A Map containing his medicines and the nr of days left for each of them 
   */
  def calculateSupplies(user_id : Long) = {
      val dosages = Dose.findAll(By(Dose.user, user_id))
      val dosageMap = scala.collection.mutable.Map[String, Long]();

      //get a map of medicine and the amount of them taken daily
      dosages.foreach(dose => {
        val medicine = dose.medicine.obj.map(medicine => medicine.toString).openOr("");
        dosageMap(medicine) = (dosageMap.getOrElseUpdate(medicine,0)+1)
      })

      //get a map of medicine and the amount in stock
      val stockMap = Stock.findAll(By(Stock.user, user_id))
                          .map(stock => (stock.medicine.obj.map(medicine => medicine.toString).openOr(""), stock.amount.is))
                          .toMap
      //merging the maps by medicine, while calculating stock/daily intake
      mergeMap(List(stockMap, makeImmutable(dosageMap)))((stock, dailyIntake) => stock / dailyIntake)
  }


}
