/*
 * Copyright 2006-2010 WorldWide Conferencing, LLC
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

package net.addictivesoftware.medicate.rest

import net.liftweb.common.Full
import scala.xml._
import net.addictivesoftware.medicate.lib.CollectionUtils
import net.addictivesoftware.medicate.model.{Schedule, Stock, Dose, User}
import net.liftweb.mapper.{Ascending, By, OrderBy}

/**
 * Utility method for Rest based classes
 */
trait RestUtils extends CollectionUtils {

  /**
   * helper method to return a text in xml
   * @param text The text to put in the error element
   * @return an Xml Element containing the error message
   */
  def errorNode(text : String) : Elem = {
    <error>{text}</error>
  }

  /**
   * Retrieves the id of the user, by looking at his uniqueId field
   * @param key equal to uniqueId field of User
   * @return the user id
   */
  def getUserIdFromKey(key:String) : Long = {
      User.find(By(User.uniqueId, key)) match {
        case Full(user) => user.id
        case (_) => 0
      }
  }

  /**
    * Helper method that returns the nr of days the user will have medicines with his current intake as a map
    * @param user_id the id of the user
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


   /**
    * Helper method that administers taking a dose
    * Stock for medicine that have a dose for the current schedule are decreased by one
    * @param user_id the id of the user
    * @param schedule the time of day the dose is taken
    */
   def takeDose(user_id:Long, schedule:Schedule.Value): Boolean = {
       Dose.findAll(By(Dose.user, user_id), By(Dose.schedule, schedule)).foreach(dose => {
           Stock.find(By(Stock.user, user_id), By(Stock.medicine, dose.medicine)) match {
             case Full(stock) => {
               stock.amount(stock.amount.is-1);
               stock.save();
               println("Reduced stock of " + stock.medicine.toString() + " by 1");
             }
             case(_) => {
               println("Could not find stock for " + dose.medicine.toString());
               false
             }
           }
       })
       true
   }

   /**
    * Helper method that adds stock
    * note that decreasing stock is possible by specifying a negative amount
    *
    * @param user_id  the user for which to add stock
    * @param medicine_id the medicine to add
    * @param amount the amount of medicine to add
    * @return true if successful
    */
   def addStock(user_id:Long, medicine_id:Long, amount:Long): Boolean = {
     Stock.find(By(Stock.user, user_id), By(Stock.medicine, medicine_id)) match {
       case Full(stock) => {
         stock.amount(stock.amount.is + amount);
         stock.save;
       }
       case (_) => {
         false
       }
     }
     true
   }

   /**
    * Helper method that gets the schedule for a day for which the user needs to take medication
    */
   def getDosageOptions(id:Long): List[String] = {
     Dose.findAll(By(Dose.user, id), OrderBy(Dose.schedule, Ascending))
       .map(_.schedule.is.toString()).distinct;
   }

}