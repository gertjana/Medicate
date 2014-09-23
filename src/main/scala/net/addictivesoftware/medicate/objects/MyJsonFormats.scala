package net.addictivesoftware.medicate

import spray.json._
import com.mongodb.casbah.commons.TypeImports.ObjectId
import spray.json.DefaultJsonProtocol._
import net.addictivesoftware.medicate.objects.MedicineObject

case class Result(result:String)
case class Error(error:String)


object MyJsonProtocol extends MyJsonProtocol

trait MyJsonProtocol extends DefaultJsonProtocol {

  //implicit val medicineObjectFormat = jsonFormat(MedicineObject,"_id", "id", "nr", "name", "brand", "amount")

  implicit val resultFormat = jsonFormat(Result, "result")

  implicit val ErrorFormat = jsonFormat(Error, "error")
 
  implicit object objectIdFormat extends RootJsonFormat[ObjectId] {
    def write(o:ObjectId) = JsString(o.toString)
    def read(value:JsValue) = new ObjectId(value.toString())
  }

  implicit object medicineObjectFormat extends RootJsonFormat[MedicineObject] {
    def write(o:MedicineObject) = JsObject(
              "_id" -> JsString(o._id.toString),
              "id" -> JsNumber(o.id),
              "nr" -> JsNumber(o.nr),
              "name" -> JsString(o.name),
              "brand" -> JsString(o.brand),
              "amount" -> JsString(o.amount)
              )
    def read(value: JsValue) = {
      value.asJsObject.getFields("_id","id", "nr", "name", "brand", "amount") match {
        case Seq(JsString(_id), JsNumber(id), JsNumber(nr), JsString(name), JsString(brand), JsString(amount)) =>
          new MedicineObject(new ObjectId(_id), id.toInt, nr.toInt, name, brand, amount)
        case _ => throw new DeserializationException("Complex expected")
      }
    }
  }
}