package test

import scala.slick.session.Session
import bestpractice.DAL

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
