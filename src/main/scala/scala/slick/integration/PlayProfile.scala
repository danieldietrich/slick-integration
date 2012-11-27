package scala.slick.integration

import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database

import play.api.Application
import play.api.Configuration
import play.api.db.DB

/**
 * Helper methods for Play implementors of the Profile trait.
 * 
 * Example:
 * 
 * {{{
 * class DAL(dbName: String) extends _DAL with ProductComponent with PlayProfile {
 * 
 *   // Profile implementation
 *   val profile = loadProfile(dbName) 
 *   def db = dbProvider(dbName)
 * 
 *   // _DAL.ddl implementation
 *   lazy val ddl: DDL = Products.ddl /*++ Xxx.ddl*/
 * 
 * }
 *
 * object DAL extends DAL("default")
 * }}}
 * 
 */
trait PlayProfile extends Profile {

  protected def loadProfile(name: String)(implicit app: play.api.Application) = {
    val config = app.configuration.getConfig("slick").getOrElse(Configuration.empty)
    val driver: String = config.getString(name + ".driver").getOrElse(throw config.reportError(name, "Missing configuration [slick." + name + ".driver]"))
    newInstance[ExtendedProfile](driver, app.classloader)
  }

  private def newInstance[T](name: String, classloader: ClassLoader)(implicit m: Manifest[T]): T =
    Class.forName(name + "$", true, classloader).getField("MODULE$").get(m.runtimeClass).asInstanceOf[T]

  protected def dbProvider(name: String)(implicit app: play.api.Application) = Database.forDataSource(DB.getDataSource(name))

}
