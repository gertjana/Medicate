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

import net.liftweb.http.rest.RestHelper
import net.liftweb.common.Full
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.mapper.By
import net.addictivesoftware.medicate.model.User
import scala.xml._

/**
 * Utility method for Rest based classes
 */
trait RestUtils {

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
}