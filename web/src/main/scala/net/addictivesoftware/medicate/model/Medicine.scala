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

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.common._
import _root_.net.liftweb.sitemap.Loc._
import _root_.net.liftweb.json._
import _root_.net.liftweb.json.JsonDSL._
import _root_.scala.xml.Node

class Medicine extends LongKeyedMapper[Medicine] with IdPK {
  def getSingleton = Medicine // what's the "meta" server

  object name extends MappedString(this, 140)
  object description extends MappedTextarea(this, 4000)
  object amount extends MappedInt(this)

  def asJson : JValue = Medicine.asJson(this)
  def asXml : Node  = Medicine.asXml(this)

  override def toString : String = name.is + " (" + amount.is + "mg)"
}


object Medicine extends Medicine with LongKeyedMetaMapper[Medicine] with CRUDify[Long, Medicine] {

  def asJson (medicine : Medicine) : JValue = {
    ("medicine" ->
      ("id" -> medicine.id.is) ~
      ("name" -> medicine.amount.is) ~
      ("description" -> medicine.description.is) ~
      ("amount" -> medicine.amount.is)
    )
  }

  def asXml (medicine : Medicine) : Node = Xml.toXml(asJson(medicine)).head




  override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.editMenuLocParams
  override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.viewMenuLocParams
  override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.createMenuLocParams
  override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.showAllMenuLocParams
}
