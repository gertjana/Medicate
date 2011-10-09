package net.addictivesoftware.medicate.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

class Medicine extends LongKeyedMapper[Medicine] with IdPK {
  def getSingleton = Medicine // what's the "meta" server

  object name extends MappedString(this, 140)
  object description extends MappedTextarea(this, 4000)
}

object Medicine extends Medicine with LongKeyedMetaMapper[Medicine] with CRUDify[Long, Medicine] {

}
