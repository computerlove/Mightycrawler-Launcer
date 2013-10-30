package models

import anorm._
import play.api.db.DB
import java.util.Date
import play.api.Play.current

case class CrawlerJobRun(id: Pk[Long] = NotAssigned)

object CrawlerJobRun {

  def create(crawlerJobId : Long ): Option[Long] =  {
    DB.withConnection { implicit c =>
      SQL("insert into crawlerjobrun (crawlerJobId, starttime) values ({crawlerJobId}, {starttime})").on(
        'starttime -> new Date(),
        'crawlerJobId -> crawlerJobId
      ).executeInsert()
    }
  }

  def updateStopTime(crawlerJobId : Long) {
    DB.withConnection { implicit c =>
      SQL("update crawlerjobrun set stoptime = {starttime} where id = {id}").on(
        'id -> crawlerJobId,
        'stoptime -> new Date()
      ).executeInsert()
    }
  }
}
