package test

import scala.slick.session.Session
import scala.slick.integration.DAL

object TestHelper {
  def running[T](block: => T): T = {
    synchronized {
      DAL.db.withSession { implicit session: Session =>
        try {
          DAL.create
          block
        } finally {
          DAL.drop
        }
      }
    }
  }
}
