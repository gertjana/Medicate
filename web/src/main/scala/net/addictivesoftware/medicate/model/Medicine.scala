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

package net.addictivesoftware.medicate.model

import net.liftweb.mapper._
import net.liftweb.http._
import net.liftweb.sitemap.Loc._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import scala.xml.Node

/**
 * Class representing a Medicine in a certain amount
 */
class Medicine extends LongKeyedMapper[Medicine] with IdPK {
  def getSingleton = Medicine

  object name extends MappedString(this, 140)
  object description extends MappedTextarea(this, 4000)
  //object amount extends MappedInt(this)

  def asJson : JValue = Medicine.asJson(this)
  def asXml : Node  = Medicine.asXml(this)

  override def toString : String = name.is
}


object Medicine extends Medicine with LongKeyedMetaMapper[Medicine] with CRUDify[Long, Medicine] {

  def asJson (medicine : Medicine) : JValue = {
    (("id" -> medicine.id.is) ~
      ("name" -> medicine.name.is) ~
      ("description" -> medicine.description.is))
  }

  def asXml (medicine : Medicine) : Node = Xml.toXml(asJson(medicine)).head

  override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.editMenuLocParams
  override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.viewMenuLocParams
  override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.createMenuLocParams
  override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.showAllMenuLocParams
}
