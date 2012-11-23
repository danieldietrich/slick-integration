package scala.slick.dao

import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database
import scala.slick.session.Session

case class User(name: String, genderage: Int, id: Option[Int] = None) extends Entity[User] {
  def withId(id: Option[Int]): User = copy(id = id)
}

class UserDAO(driver: ExtendedProfile, databaseProvider: () => Database) extends BaseDAO[User](driver, databaseProvider) {

  object Users extends Mapper("user") {

    def name = column[String]("name")
    def age = column[Int]("age")

    def * = name ~ age ~ id <> (User, User.unapply _)
    
    protected def finderByName = createFinderBy(_.name)
    
    def findUserByName(name: String) = db.withSession { implicit s: Session =>
      finderByName(name).list
    }

  }

}

object Test extends App {

  val user = User("Daniel", 37).withId(Some(1))

  println(user)

  assert(user == User("Daniel", 37, Some(1)))

}
