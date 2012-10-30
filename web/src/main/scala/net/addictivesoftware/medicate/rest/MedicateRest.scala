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

import net.liftweb.http.{XmlResponse,JsonResponse, S}
import net.liftweb.http.rest.RestHelper
import net.addictivesoftware.medicate.model.{User, Stock, Dose,  Medicine, Schedule}
import net.liftweb.common.Full
import net.liftweb.mapper.{By, OrderBy, Ascending}
import net.liftweb.util.Helpers.AsLong
import net.liftweb.json.{JArray, JString, JValue}
import net.liftweb.json.JsonDSL._

/**
 * Main REST interface
 * 
 * Extended with Liftweb's Resthelper and our own @see RestUtils
 */
object MedicateRest extends RestHelper with RestUtils {

  // generic query-ing of medicine
  serve( "api" / "1.0" / "medicine" prefix {
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
  serve ("api" / "1.0" / "auth" prefix {
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
  serve ( "api" / "1.0" / "user" prefix {
    //return dosages
    case key :: "dosages" :: _ XmlGet _=> {
      var id = getUserIdFromKey(key)
      <dosages>
        {
          Dose.findAll(By(Dose.user, id), OrderBy(Dose.schedule, Ascending)).map(dose => dose.asXml)
        }
      </dosages>
    }
    case key :: "dosages" :: _ JsonGet _=> {
      var id = getUserIdFromKey(key)
      ("dosages" -> Dose.findAll(By(Dose.user, id), OrderBy(Dose.schedule, Ascending)).map(dose => dose.asJson)) : JValue
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
      val supplies:Map[String, Double] = calculateSupplies(id)

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

  serve ( "api" / "1.0" / "user" prefix {

    // take dose
    case key :: "takedose" :: scheduleString :: _ XmlGet _ => {
        <result>{
          val userId = getUserIdFromKey(key);
          takeDose(userId, Schedule.withName(scheduleString));
        }</result>
    }

    case key :: "takedose" :: scheduleString :: _ JsonGet _ => {
      val userId = getUserIdFromKey(key);
      ("Result", takeDose(userId, Schedule.withName(scheduleString))) :JValue;
    }

    // add stock
    case key :: "addstock" :: _ XmlGet _ => {
      for {
        medicineId <- S.param("medicine") ?~ "medicine param is mandatory"
        amount <- S.param("amount") ?~ "amount param is mandatory"
      } yield {
        <result>{
          val id = getUserIdFromKey(key);
          addStock(id, medicineId.toLong, amount.toLong)
        }</result>
      }
    }
    case key :: "addstock" :: _ JsonGet _ => {
      for {
        medicineId <- S.param("medicine") ?~ "medicine param is mandatory"
        amount <- S.param("amount") ?~ "amount param is mandatory"
      } yield {
        val id = getUserIdFromKey(key);
        ("Result", addStock(id, medicineId.toLong, amount.toLong)):JValue;
      }
    }

    case key :: dosageoptions :: _ XmlGet _ => {
      <dosageOptions>{
        val id = getUserIdFromKey(key);
        val options:List[String] = getDosageOptions(id);
        options.map(option => <option>{option}</option>)
      }</dosageOptions>
    }

    case key :: dosageoptions :: _ JsonGet _ => {
        val id = getUserIdFromKey(key);
        val options: List[String] = getDosageOptions(id);
        ("DosageOptions" -> new JArray(options.map(new JString(_)))) : JValue
    }

  })


  
}
