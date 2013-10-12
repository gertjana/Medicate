package net.addictivesoftware.medicate

import spray.json.{JsValue, JsString, JsonFormat}
import com.mongodb.casbah.commons.TypeImports.ObjectId
import spray.json.DefaultJsonProtocol._
import net.addictivesoftware.medicate.objects.MedicineObject

case class Result(result:String)
case class Error(error:String)


object MyJsonProtocol {

  implicit val medicineObjectFormat = jsonFormat(MedicineObject,"_id", "id", "number", "name", "brand", "amount")

  implicit val resultFormat = jsonFormat(Result, "result")

  implicit val ErrorFormat = jsonFormat(Error, "error")
 
  implicit val objectIdFormat:JsonFormat[ObjectId] = new JsonFormat[ObjectId] {
    def write(o:ObjectId) = JsString(o.toString)
    def read(value:JsValue) = new ObjectId(value.toString())
  }

}