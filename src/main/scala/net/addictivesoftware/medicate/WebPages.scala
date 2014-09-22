
package net.addictivesoftware.medicate

import net.addictivesoftware.medicate.objects.MedicineObject
import java.util.Date
import java.text.SimpleDateFormat

/**
 * Trait that contains the (static) webpages
 */
trait WebPages {
  private val dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss:SSS z")


  /**
   * Lists events
   */
  def listHtmlPage(medicines:List[MedicineObject]) =
    <html>
      <head>
        <title>Flow - List Events</title>
        <link href="/flow/css/main.css" rel="stylesheet" type="text/css"/>
      </head>
      <body>
        <h3>List Events:</h3>
        <table class="event-list">
          <tr>
            <th>Id</th>
            <th>Number</th>
            <th>Name</th>
            <th>Brand</th>
            <th>Amount</th>
          </tr>
          {
          medicines.map(
            medicine => {
              <tr>
                <td>{medicine.id}</td>
                <td>{medicine.nr}</td>
                <td>{medicine.name}</td>
                <td>{medicine.brand}</td>
                <td>{medicine.amount}</td>
              </tr>
            }
          )
          }
        </table>
      </body>
    </html>
}

