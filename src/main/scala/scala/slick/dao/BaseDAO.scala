package scala.slick.dao

import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database

protected object PK {
  type Type = Int
}

trait Entity[T <: Entity[T]] {
  val id: Option[PK.Type]
  def withId(id: Option[PK.Type]): T
}

abstract class BaseDAO[T <: Entity[T]](val driver: ExtendedProfile, databaseProvider: () => Database) {

  import driver.simple._

  def db: Database = databaseProvider()

  abstract class Mapper(table: String) extends Table[T](None, table) {

    // table columns 
    def id = column[Option[PK.Type]]("id", O.PrimaryKey, O.AutoInc)

    // helpers
    protected def autoInc = * returning id
    protected def finderById = this.createFinderBy(_.id.get)

    // operations on rows
    def delete(id: PK.Type): Int = db.withSession { implicit s: Session =>
      this.where(_.id === id).delete
    }

    def findAll(): List[T] = db.withSession { implicit s: Session =>
      (for (entity <- this) yield entity).list
    }

    def findById(id: PK.Type): Option[T] = db.withSession { implicit s: Session =>
      finderById(id).firstOption
    }

    def insert(entity: T): T = db.withSession { implicit s: Session =>
      val id = autoInc.insert(entity)
      entity.withId(id)
    }

    def update(entity: T): Int = db.withSession { implicit s: Session =>
      entity.id.map { id =>
        this.where(_.id === id).update(entity)
      }.getOrElse(0)
    }
  }

}
