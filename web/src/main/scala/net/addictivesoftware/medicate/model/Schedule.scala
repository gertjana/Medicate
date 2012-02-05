package net.addictivesoftware.medicate.model

/**
 * Created by IntelliJ IDEA.
 * User: gertjan
 * Date: 2/5/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */

object Schedule extends Enumeration {
  type Schedule = Value
  val Wakeup, Breakfast, Lunch, Dinner, BeforeSleep = Value
}
