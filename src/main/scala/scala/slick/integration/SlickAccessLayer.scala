package scala.slick.integration

import scala.slick.driver.ExtendedProfile
import scala.slick.lifted.DDL
import scala.slick.session.Database

/**
 * Mix-in used with _Components and _DAL.
 */
trait Profile {
  val profile: ExtendedProfile
  def db: Database
}

/**
 * The Data Access Layer (DAL) is the interface to the data access layer which
 * contains entities (= case classes) and operations on entities (= table mappers,
 * encapsulated by components).
 * 
 * The DAL is the cake of the cake pattern, mixing several _Components which depend
 * on Profile, where Profile encapsulates the dependency to the slick driver.
 * 
 * {{{
 * import Profile._
 * 
 * object DAL extends _DAL with Component1 with Component2 with ... with Profile {
 * 
 *     // trait Profile implementation
 *   override val profile = loadProfile("default") 
 *   override def db = dbProvider("default")
 *
 *   // _DAL.ddl implementation
 *   override lazy val ddl: DDL = Component1.ddl ++ Component2.ddl ++ ...
 *
 * } 
 * }}}
 * 
 */
trait _DAL { self: Profile =>

  import profile.simple._

  // List of DDL's of all Components (Comp1.ddl ++ Comp2.ddl ++ ...)
  val ddl: DDL

  /**
   * Create database
   */
  def create(implicit s: Session): Unit = ddl.create
  
  /**
   * Drop database
   */
  def drop(implicit s: Session): Unit = ddl.drop

}

/**
 * Here, a Database entity has an auto generated
 * id and is be copied by withId on insertion.
 */
trait Entity[T <: Entity[T]] {
  val id: Option[Long]
  def withId(id: Long): T
}

/**
 * Because a Slick Table depends on scala.slick.session.Driver,
 * the _Components have to be mixed in a DAL with the Cake pattern.
 */
trait _Component { self: Profile =>

  import profile.simple._

  /**
   * Default table columns and operations.
   */
  abstract class Mapper[T <: Entity[T]](table: String) extends Table[T](None, table) {

    // table columns
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    // helpers
    protected def autoInc = * returning id
    
    // operations on rows
    def delete(id: Long): Boolean = db.withSession { implicit s: Session =>
      this.filter(_.id === id.bind).delete > 0
    }
    
    def findAll(): List[T] = db.withSession { implicit s: Session =>
      (for (entity <- this) yield entity).list
    }

    def findById(id: Long): Option[T] = db.withSession { implicit s: Session =>
      (for { e <- this if e.id === id.bind} yield e).firstOption
    }

    def insert(entity: T): T = db.withSession { implicit s: Session =>
      val id = autoInc.insert(entity)
      entity.withId(id)
    }

    def update(entity: T): T = db.withSession { implicit s: Session =>
      entity.id.map { id =>
        this.filter(_.id === id.bind).update(entity)
      }
      entity
    }
  }
}
