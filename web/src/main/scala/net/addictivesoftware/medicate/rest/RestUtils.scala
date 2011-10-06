package net.addictivesoftware.medicate.rest

import net.liftweb.http.rest.RestHelper
import net.liftweb.common.Full
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import net.liftweb.mapper.By
import net.addictivesoftware.medicate.model.User
import scala.xml._


trait RestUtils {

  def JsonWrapper(name : String, content : JValue) : JValue = {
    (name -> content)
  }

  def errorNode(text : String) : Elem = {
    <error>{text}</error>
  }

  def getUserIdFromKey(key:String) : Long = {
      User.find(By(User.uniqueId, key)) match {
        case Full(user) => user.id
        case (_) => 0
      }
  }
}