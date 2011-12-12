package net.addictivesoftware.medicate

import org.specs2.mutable.SpecificationWithJUnit
import net.addictivesoftware.medicate.model.{Medicine, Dose, Stock, Schedule, User}
import net.addictivesoftware.medicate.rest.MedicateRest
import net.liftweb.mapper.By
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner


@RunWith(classOf[JUnitRunner])
class ModelTest extends SpecificationWithJUnit  {

  // initialise an in memory h2 database
  InMemoryDB.init;

  // create some medicines
  val methformine = Medicine.create.name("Methformine").amount(500).saveMe;
  val glimepiride = Medicine.create.name("Glimepiride").amount(2).saveMe;
  val omezopranol = Medicine.create.name("Omezopranol").amount(20).saveMe;

  //Create a user
  val user = User.create.firstName("Theo").lastName("Tester").email("theo@tester.com").password("test").saveMe;

  //create some dosages/stock for this user
  Dose.create.user(user).medicine(glimepiride).schedule(Schedule.Breakfast).save;
  Dose.create.user(user).medicine(methformine).schedule(Schedule.Breakfast).save;
  Dose.create.user(user).medicine(methformine).schedule(Schedule.Dinner).save;
  Stock.create.user(user).medicine(methformine).amount(90).save;
  Stock.create.user(user).medicine(glimepiride).amount(90).save;

  "The Database" should {
    "contain 3 medicines" in {
      Medicine.findAll() must have size(3)
    }
  }

  "The Database" should {
    "contain 3 dosages" in {
      Dose.findAll() must have size(3)
    }
  }

  "The Database" should {
    "contain 2 stock" in {
      Stock.findAll() must have size(2)
    }
  }

  "The toString() of Methformine" should {
    "read Methformine (500mg)" in {
      val medicines : List[Medicine] =  Medicine.findAll(By(Medicine.name, "Methformine"));
      medicines.head.toString() must be equalTo("Methformine (500mg)")
    }
  }
  
  "The toString() of Glimepiride" should {
    "read Glimepiride (2mg)" in {
      val medicines : List[Medicine] =  Medicine.findAll(By(Medicine.name, "Glimepiride"));
      medicines.head.toString() must be equalTo("Glimepiride (2mg)")
    }
  }

  "Supplies of user " should {
    "contain 2 items" in {
      val supplies:Map[String, Long] = MedicateRest.calculateSupplies(user.id);
      println(supplies)
      supplies.size must be equalTo(2)

    }
  }
  
  "First supply" should {
    "contain 45 days of Methformine" in {
      val supplies:Map[String, Long] = MedicateRest.calculateSupplies(user.id);
      supplies.head._1 must be equalTo("Methformine (500mg)")
      supplies.head._2 must be equalTo(45)
    }
  }
  
  "second supply" should {
    "contain 90 days of Glimepiride" in {
      val supplies:Map[String, Long] = MedicateRest.calculateSupplies(user.id);
      supplies.tail.head._1 must be equalTo("Glimepiride (2mg)")
      supplies.tail.head._2 must be equalTo(90)
    }
  }
}

