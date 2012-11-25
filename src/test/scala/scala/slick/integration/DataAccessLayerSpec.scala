package scala.slick.integration

import org.specs2.mutable._
import play.api.db.DB
import scala.slick.driver.H2Driver
import scala.slick.lifted.DDL
import scala.slick.session._
import org.specs2.execute.Result
import org.specs2.specification.AroundOutside

case class Product(name: String, description: String, id: Option[Int] = None) extends Entity[Product] {
  def withId(id: Int): Product = copy(id = Some(id))
}

trait ProductComponent extends _Component[Product] { self: Profile =>

  object Products extends Mapper("product") {

    def name = column[String]("name")
    def description = column[String]("description")

    def * = name ~ description ~ id.? <> (Product, Product.unapply _)

  }

}

object DAL extends _DAL with ProductComponent with Profile {

  // trait Profile implementation
  override val profile = H2Driver // PlayProfile.loadProfile("default") 
  override def db = Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") // PlayProfile.dbProvider("default")

  // _DAL.ddl implementation
  override lazy val ddl: DDL = Products.ddl // ++ Others.ddl

}

class DataAccessLayerSpec extends Specification {

  import DAL._
  
  "Slick Integration" should {
    "be testable with in-memory h2 driver" in {
      db.withSession { s: Session =>
        s.metaData.getDatabaseProductName must equalTo("H2")
      }
    }
  }

  "Product model" should {
    "crud" in {
      db.withSession { implicit s: Session =>
        
        create
        
        val product = Products.insert(Product("name", "description"))
        product.id must not equalTo (None) // AutoInc id correct
        product.id.map { id =>
          Products.findById(id) must equalTo(Some(Product("name", "description", Some(id)))) // product found
          Products.delete(id) must equalTo(1) // one row deleted 
          Products.findById(id) must equalTo(None) // product not found
        }
        
        drop
        
        success

      }
    }
  }

}
