package scala.slick.integration

import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database

/**
 * Helper methods for Play implementors of the Profile trait.
 *
 * Example:
 *
 * import scala.slick.integration._DAL
 * import scala.slick.integration.SimpleProfile
 * import scala.slick.lifted.DDL
 * import scala.slick.session.Database
 * import play.api.Play.{current => app}
 * import play.api.db.DB
 * 
 * {{{
 * class DAL(dbName: String) extends _DAL with ProductComponent with SimpleProfile {
 *
 *   override val profile = {
 *     val conf = app.configuration
 *     val name = "db." + dbName + ".slick.driver"
 *     val driver = conf.getConfig(name).getOrElse(throw conf.reportError(name, "Missing configuration [" + name + "]"))
 *     load(driver)
 *   }
 *
 *   override def db = Database.forDataSource(DB.getDataSource(dbName))
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
trait SimpleProfile extends Profile {

  protected def load(driver: String) = {
    
    def newInstance[T](name: String)(implicit m: Manifest[T]): T =
      Class.forName(name + "$").getField("MODULE$").get(m.runtimeClass).asInstanceOf[T]

    newInstance[ExtendedProfile](driver)
    
  }

}
