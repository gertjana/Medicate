package net.addictivesoftware.medicate

import org.specs2.mutable._

class MedicatePropertiesSpec extends Specification {

  "The Medicate Properties class" should {

    "loading medicateProperties should have 7 items" in {
      MedicateProperties.medicateProperties.size mustEqual 7
    }

    "loading some OPENSHIFT_* should result in the correct values" in {
      MedicateProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_PORT").toInt mustEqual 27017
      MedicateProperties.getEnvOrProp("OPENSHIFT_MONGODB_DB_USERNAME") mustEqual "admin"
   }

    "loading an env variable should work fine" in {
    	MedicateProperties.getEnvOrProp("HOME") must not beNull
    }
  }
}