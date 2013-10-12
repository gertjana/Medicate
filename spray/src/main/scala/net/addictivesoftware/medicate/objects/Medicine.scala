package net.addictivesoftware.medicate.objects

import com.novus.salat.annotations.raw.Key
import com.novus.salat.dao.SalatDAO
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import scala.Some
import net.addictivesoftware.medicate.MedicateMongoConnection

case class MedicineObject (
  @Key("id") _id:ObjectId = new ObjectId(),
  id:Int,
  nr:Int,
  name:String,
  brand:String,
  amount:String
)

object MedicineDAO extends SalatDAO[MedicineObject, ObjectId](collection = MedicateMongoConnection.getMedicineCollection)

object Medicine {

  def insert(medicine:MedicineObject):Option[ObjectId] = {
    MedicineDAO.insert(medicine)
  }

  def delete(medicine:MedicineObject) = {
    MedicineDAO.remove(medicine)
  }

  def deleteById(id:String) = {
    getById(id) match {
      case Some(medicine) => { delete(medicine) }
      case _ => {}
    }
  }

  def update(id: ObjectId, medicine:MedicineObject) = {
    MedicineDAO.update(MongoDBObject("_id" -> id), grater[MedicineObject].asDBObject(medicine))
  }

  def getById(id:String):Option[MedicineObject] = {
    MedicineDAO.find(MongoDBObject("_id" -> id))
      .limit(1)
      .toList
      .headOption
  }

  def list():List[MedicineObject] = {
    MedicineDAO.find(MongoDBObject()).toList
  }
}