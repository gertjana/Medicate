package net.addictivesoftware.medicate.model

object Schedule extends Enumeration {
  type Schedule = Value
  val Wakeup, Breakfast, Lunch, Dinner, BeforeSleep = Value
}
