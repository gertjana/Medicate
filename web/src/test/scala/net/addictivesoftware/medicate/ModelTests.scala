package net.addictivesoftware.medicate

import org.specs2.mutable.SpecificationWithJUnit
import net.addictivesoftware.medicate.model.{Medicine, Dose, Stock, Schedule, User}
import net.addictivesoftware.medicate.rest.MedicateRest
import net.liftweb.mapper.By
import net.liftweb.common.Full
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner


@RunWith(classOf[JUnitRunner])
class ModelTest extends SpecificationWithJUnit  {

  // initialise an in memory h2 database
  InMemoryDB.init;

  // create some medicines
  val methformine = Medicine.create.name("Methformine").saveMe();
  val glimepiride = Medicine.create.name("Glimepiride").saveMe();
  val omezopranol = Medicine.create.name("Omezopranol").saveMe();

  //Create a user
  val user = User.create.firstName("Theo").lastName("Tester").email("theo@tester.com").password("test").saveMe();

  //create some dosages/stock for this user
  Dose.create.user(user).medicine(glimepiride).amount(2).schedule(Schedule.Breakfast).save();
  Dose.create.user(user).medicine(methformine).amount(500).schedule(Schedule.Breakfast).save();
  Dose.create.user(user).medicine(methformine).amount(500).schedule(Schedule.Dinner).save();
  Dose.create.user(user).medicine(glimepiride).amount(1).schedule(Schedule.Dinner).save();

  Stock.create.user(user).medicine(methformine).dosage(500).amount(90).save();
  Stock.create.user(user).medicine(glimepiride).dosage(2).amount(90).save();

  "The Database" should {
    "contain 3 medicines" in {
      Medicine.findAll() must have size(3)
    }
  }

  "The Database" should {
    "contain 3 dosages" in {
      Dose.findAll() must have size(4)
    }
  }

  "The Database" should {
    "contain 2 stock" in {
      Stock.findAll() must have size(2)
    }
  }

  "The toString() of Methformine" should {
    "read Methformine" in {
      val medicines : List[Medicine] =  Medicine.findAll(By(Medicine.name, "Methformine"));
      medicines.head.toString() must be equalTo("Methformine")
    }
  }
  
  "The toString() of Glimepiride" should {
    "read Glimepiride" in {
      val medicines : List[Medicine] =  Medicine.findAll(By(Medicine.name, "Glimepiride"));
      medicines.head.toString() must be equalTo("Glimepiride")
    }
  }

  "Supplies of user " + user.fullName should {
    "contain 2 items" in {
      val supplies:Map[String, Double] = MedicateRest.calculateSupplies(user.id);
      supplies.size must be equalTo(2)
    }
  }
  
  "First supply" should {
    "contain 45 days of Methformine" in {
      val supplies:Map[String, Double] = MedicateRest.calculateSupplies(user.id);
      supplies.head._1 must be equalTo("Methformine")
      supplies.head._2 must be equalTo(45)
    }
  }
  
  "Second supply" should {
    "contain 60 days of Glimepiride" in {
      val supplies:Map[String, Double] = MedicateRest.calculateSupplies(user.id);
      supplies.tail.head._1 must be equalTo("Glimepiride")
      supplies.tail.head._2 must be equalTo(60)
    }
  }
  
  "Taking a dose at breakfast and one at dinner" should {
    "reduce the supplies of glimepiride by 1.5, and methformine by two" in {
      MedicateRest.takeDose(user.id, Schedule.Breakfast);
      MedicateRest.takeDose(user.id, Schedule.Dinner);

      val supplies:Map[String, Double] = MedicateRest.calculateSupplies(user.id);
      supplies.head._1 must be equalTo("Methformine")
      supplies.head._2 must be equalTo(44)
      supplies.tail.head._1 must be equalTo("Glimepiride")
      supplies.tail.head._2 must be equalTo(59)
    }
  }
  
  
  "Adding stock for glimepiride" should {
    "increase the amount by 90" in {
      MedicateRest.addStock(user.id, glimepiride.id, 90);

      Stock.find(By(Stock.medicine, glimepiride.id), By(Stock.user, user.id)) match {
        case Full(stock) => { stock.amount.is must be equalTo(178.5) }
        case (_) => { failure("No Stock found for Glimepiride") }
      }

    }
  }

  "Adding stock for methformine" should {
    "increase the amount by 90" in {
      MedicateRest.addStock(user.id, methformine.id, 90);

      Stock.find(By(Stock.medicine, methformine.id), By(Stock.user, user.id)) match {
        case Full(stock) => { stock.amount.is must be equalTo(178) }
        case (_) => { failure("No Stock found for Methformine") }
      }
    }
  }

  "Getting the times a dosage needs to be taken" should {
    "return 2 items: Breakfast and Dinner" in {
      val options = MedicateRest.getDosageOptions(user.id);
      options.size must be equalTo(2)
      options.head must be equalTo("Breakfast");
      options.tail.head must be equalTo("Dinner");
    }
  }

}

