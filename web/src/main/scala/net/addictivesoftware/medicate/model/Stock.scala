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

/**
 * class representing the amount of Medicine a user has in stock
 */
class Stock extends LongKeyedMapper[Stock] with IdPK {
  def getSingleton = Stock // what's the "meta" server

  object user extends MappedLongForeignKey(this, User) {
    override def validSelectValues = { Full(for (user <- User.findAll) yield (user.id.is, user.fullName)) }
  }

  object medicine extends MappedLongForeignKey(this, Medicine) {
    override def validSelectValues = { Full(for (medicine <- Medicine.findAll) yield (medicine.id.is, medicine.name.is)) }
  }

  object amount extends MappedLong(this)

  def asJson : JValue = Stock.asJson(this)
  def asXml : Node  = Stock.asXml(this)

}

object Stock extends Stock with LongKeyedMetaMapper[Stock] with CRUDify[Long, Stock] {

  def asJson (stock : Stock) : JValue = {
     ("stock" ->
       ("id" -> stock.id.is) ~
       ("medicine" -> stock.medicine.obj.map(medicine => medicine.toString).openOr("")) ~
       ("amount" -> stock.amount.is)
     )
   }

  def asXml (stock : Stock) : Node = Xml.toXml(asJson(stock)).head

  override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.editMenuLocParams
  override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.viewMenuLocParams
  override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.createMenuLocParams
  override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.showAllMenuLocParams
}