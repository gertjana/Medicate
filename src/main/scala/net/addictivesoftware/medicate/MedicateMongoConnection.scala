package net.addictivesoftware.medicate

import com.mongodb.casbah.Imports._

object MedicateMongoConnection {
  val mongoHost             = MedicateProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_HOST")
  val mongoPort             = MedicateProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PORT") toInt
  val mongoUser             = MedicateProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_USERNAME")
  val mongoPassword         = MedicateProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PASSWORD")

  def getMedicineCollection() = {
    val db = MongoConnection(mongoHost, mongoPort).getDB("medicate")
    try {
      db.authenticate(mongoUser, mongoPassword)
    } catch {
      case _:Exception => {} // ignoring authentication exception when authentication is turned off on the mongodb server
    }

    val collection = db("medicine")
    collection
  }
}