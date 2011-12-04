package net.addictivesoftware.medicate

import net.liftweb.mapper._
import net.liftweb.common._
import net.addictivesoftware.medicate.model._

object InMemoryDB {

  val vendor = new StandardDBVendor("org.h2.Driver",
    "jdbc:h2:mem:lift_tests;DB_CLOSE_DELAY=-1", Empty, Empty)

  Logger.setup = Full(net.liftweb.util.LoggingAutoConfigurer())
  Logger.setup.foreach { _.apply() }

  def init {
    DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    Schemifier.destroyTables_!!(Schemifier.infoF _, User, Medicine, Dose, Stock)
    Schemifier.schemify(true, Schemifier.infoF _, User, Medicine, Dose, Stock)
  }
  def shutdown {

  }
}