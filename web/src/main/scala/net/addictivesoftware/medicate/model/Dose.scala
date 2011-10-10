package net.addictivesoftware.medicate.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.common._
import _root_.net.liftweb.sitemap.Loc._

class Dose extends LongKeyedMapper[Dose] with IdPK {
   def getSingleton = Dose

   object user extends MappedLongForeignKey(this, User) {
     override def validSelectValues = { Full(for (user <- User.findAll) yield (user.id.is, user.fullName)) }
   }
   object medicine extends MappedLongForeignKey(this, Medicine) {
     override def validSelectValues = { Full(for (medicine <- Medicine.findAll) yield (medicine.id.is, medicine.name.is)) }
   }
   object amount extends MappedLong(this)
   object schedule extends MappedEnum(this, Schedule)

}

object Dose extends Dose with LongKeyedMetaMapper[Dose] with CRUDify[Long, Dose] {
  override def editMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.editMenuLocParams
  override def viewMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.viewMenuLocParams
  override def createMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.createMenuLocParams
  override def showAllMenuLocParams = If(User.loggedIn_? _, RedirectResponse("/")) :: super.showAllMenuLocParams
}

object Schedule extends Enumeration {
    type Schedule = Value
    val Wakeup, Breakfast, Lunch, Dinner, BeforeSleep = Value
}