package models

import anorm._
import play.api.db.DB
import java.util.Date
import play.api.Play.current
import anorm.SqlParser._
import anorm.~

case class CrawlerJobRun(id: Pk[Long] = NotAssigned, crawlerJobId : Long, starttime : Date, stoptime: Option[Date])

object CrawlerJobRun {

  def allForJob(crawlerJobId: Long): List[CrawlerJobRun] = DB.withConnection { implicit c =>
    DB.withConnection { implicit c =>
      SQL("select * from crawlerjobrun where crawlerJobId = {crawlerJobId}").on(
        'crawlerJobId -> crawlerJobId
      ).as(crawlerjobrun *)
    }
  }

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
      SQL("update crawlerjobrun set stoptime = {stoptime} where crawlerJobId = {id}").on(
        'id -> crawlerJobId,
        'stoptime -> new Date()
      ).executeInsert()
    }
  }

  private val crawlerjobrun = {
    get[Pk[Long]]("crawlerJobRunId") ~
    get[Long]("crawlerJobId") ~
    get[Date]("starttime") ~
    get[Option[Date]]("stoptime") map {
      case crawlerJobRunId~crawlerJobId~starttime~stoptime
        => CrawlerJobRun(crawlerJobRunId, crawlerJobId, starttime, stoptime)
    }
  }
}
