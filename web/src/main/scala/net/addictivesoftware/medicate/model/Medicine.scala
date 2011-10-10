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
