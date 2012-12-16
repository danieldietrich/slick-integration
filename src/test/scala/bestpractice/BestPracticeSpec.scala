package bestpractice

import org.specs2.mutable._
import scala.slick.driver.H2Driver
import scala.slick.integration.{ _Component, _DAL, Entity, Profile }
import scala.slick.lifted.DDL
import scala.slick.session._
import test.TestHelper

/**
 * Nullable vs Non-Nullable Table Columns
 * 
 *  case class Entity           | Table[Entity] column        | def * = ...        | use case
 * -----------------------------+-----------------------------+--------------------+----------------------------------------
 *  xxx: Type                   | column[Type]("xxx")         | xxx                | default non-nullable column case
 *  xxx: Type                   | column[Option[Type]]("xxx") | xxx.getOr(default) | ???
 *  xxx: Option[Type] = default | column[Type]("xxx", ...)    | xxx.?              | values provided by db at creation time (i.e. auto incremented pk)
 *  xxx: Option[Type] = default | column[Option[Type]]("xxx") | xxx                | default nullable column case
 */
case class HandleNull(name: Option[String] = None, id: Option[Long] = None)

trait HandleNullComponent extends _Component { self: Profile =>

  import profile.simple._

  object HandleNulls extends Table[HandleNull]("handle_null") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[Option[String]]("name")
    def * = name ~ id.? <> (HandleNull, HandleNull.unapply _)
  }
  
}

object DAL extends _DAL with HandleNullComponent with Profile {

  // trait Profile implementation
  val profile = H2Driver

  private val database = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver")
  def db = database

  // _DAL.ddl implementation
  lazy val ddl: DDL = HandleNulls.ddl

  db.withSession { implicit s: Session =>
    import profile.simple._
    ddl.create
  }

}

object BestPracticeSpec {

  // TODO
  
}
