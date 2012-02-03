/*
 * Copyright 2006-2011 Addictive Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper._
import _root_.java.sql.{Connection, DriverManager}
import _root_.net.addictivesoftware.medicate.model._
import _root_.net.addictivesoftware.medicate.rest._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    //determine which db to use in the following order
    // 1. JNDI
    // 2. Cloudfoundry
    // 3. username.hostname.props file on the classpath
    // 4. default.props on the classpath
    // 5. H2 in memory database
    if (!DB.jndiJdbcConnAvailable_?) {

      val connection = CloudFoundryConnection
      connection.newConnection(DefaultConnectionIdentifier) match {

        case Empty => {
          val vendor = new StandardDBVendor(
            Props.get("db.class") openOr "org.h2.Driver",
            Props.get("db.url") openOr "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
            Props.get("db.user"),
            Props.get("db.password")
          )
          LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)
          DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
        }

        case (_) => {
          DB.defineConnectionManager(DefaultConnectionIdentifier, connection)
        }
      }
    }

    // where to search snippet
    LiftRules.addToPackages("net.addictivesoftware.medicate")
    Schemifier.schemify(true, Schemifier.infoF _, User, Medicine, Dose, Stock)

    val home = Loc("HomePage", "index" :: Nil, "Home Page", Hidden)
    val restapi = Loc("RestApiPage", "restapi" :: Nil, "Rest Api")
    val about = Loc("AboutPage", "about" :: Nil, "About")
    val crudMenu = Medicine.menus ::: Dose.menus ::: Stock.menus
    val allMenus = Menu(home) :: Menu(restapi) :: Menu(about) :: User.sitemap
    val mySiteMap = SiteMap((allMenus ::: crudMenu): _*)


    // Build SiteMap
    def sitemap() = //SiteMap(
      mySiteMap
 //     Menu("Home") / "index" >> User.AddUserMenusAfter, // Simple menu form
 //     // Menu with special Link
 //     Menu(Loc("Static", Link(List("static"), true, "/static/index"),
//       "Static Content"))
  //  )
    LiftRules.setSiteMap(mySiteMap)
    //LiftRules.setSiteMapFunc(() => User.sitemapMutator(sitemap()))

    LiftRules.dispatch.append(MedicateRest)
    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    S.addAround(DB.buildLoanWrapper)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

  object CloudFoundryConnection extends ConnectionManager {
    def newConnection(name: ConnectionIdentifier): Box[Connection] = {
      try {
        import org.cloudfoundry.runtime.env._
        import org.cloudfoundry.runtime.service.relational._
        Full(new MysqlServiceCreator(new CloudEnvironment())
                        .createSingletonService().service.getConnection())

      } catch {
        case e : Exception => Empty
      }
    }
    def releaseConnection(conn: Connection) {conn.close}
  }
}
