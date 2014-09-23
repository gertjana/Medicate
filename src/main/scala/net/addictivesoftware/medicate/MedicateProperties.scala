package net.addictivesoftware.medicate

object MedicateProperties extends Utilities {
  var propFilename = "/medicate.properties"
  val hostName = Option(java.net.InetAddress.getLocalHost().getHostName)


  def getString(name:String): String = {
    medicateProperties.getProperty(name)
  }

    def getInt(name:String): Int = {
    medicateProperties.getProperty(name).toInt
  }

  def getEnvOrProp(name: String) : String = {
    Option(System.getenv(name)) match {
      case Some(value) =>
        value
      case _ =>
        getString(name)
    }
  }


 lazy val medicateProperties: java.util.Properties = {
    val properties = new java.util.Properties

    hostName match {
      case Some(name) => propFilename = "/medicate-" + name.toLowerCase + ".properties"
      case _ => {}
    }
    var fileExists:Boolean = false;

    using( getClass.getResourceAsStream(propFilename) ) {stream =>
      properties.load(stream)
      fileExists = true;
    }

    if (!fileExists) {
      using( getClass.getResourceAsStream("/medicate.properties") ) {stream =>
        properties.load(stream)
      }      
    }

    properties
  }
}
