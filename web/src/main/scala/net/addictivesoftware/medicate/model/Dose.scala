package net.addictivesoftware.medicate.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.common._
import _root_.net.liftweb.sitemap.Loc._
import _root_.net.liftweb.json._
import _root_.net.liftweb.json.JsonDSL._
import _root_.scala.xml.Node

class Dose extends LongKeyedMapper[Dose] with IdPK {
  def getSingleton = Dose

  object user extends MappedLongForeignKey(this, User) {
   override def validSelectValues = { Full(for (user <- User.findAll) yield (user.id.is, user.fullName)) }
  }
  object medicine extends MappedLongForeignKey(this, Medicine) {
   override def validSelectValues = { Full(for (medicine <- Medicine.findAll) yield (medicine.id.is, medicine.name.is)) }
  }

  object schedule extends MappedEnum(this, Schedule)

  def asJson : JValue = Dose.asJson(this)
  def asXml : Node  = Dose.asXml(this)
}

object Dose extends Dose with LongKeyedMetaMapper[Dose] with CRUDify[Long, Dose] {
  def asJson (dose : Dose) : JValue = {
    ("dose" ->
      ("id" -> dose.id.is) ~
      ("medicine" -> dose.medicine.obj.map(medicine => medicine.toString).openOr("")) ~
      ("schedule" -> dose.schedule.is.toString())
    )
  }

  def asXml (dose : Dose) : Node = Xml.toXml(asJson(dose)).head

  override def editMenuLocParams    = RedirectIfNotLoggedIn :: super.editMenuLocParams
  override def viewMenuLocParams    = RedirectIfNotLoggedIn :: super.viewMenuLocParams
  override def createMenuLocParams  = RedirectIfNotLoggedIn :: super.createMenuLocParams
  override def showAllMenuLocParams = RedirectIfNotLoggedIn :: super.showAllMenuLocParams

  def RedirectIfNotLoggedIn = {
    If(User.loggedIn_? _, RedirectResponse("/"))
  }
}

object Schedule extends Enumeration {
    type Schedule = Value
    val Wakeup, Breakfast, Lunch, Dinner, BeforeSleep = Value
}