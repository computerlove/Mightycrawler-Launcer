package models

import anorm._
import play.api.db.DB
import play.api.Play.current
import anorm.SqlParser._
import anorm.~

import util.AnormExtension._
import java.time.LocalDateTime

case class CrawlerJobRun(id: Pk[Long] = NotAssigned, crawlerJobId : Long, starttime : LocalDateTime, stoptime: Option[LocalDateTime])
case class NonOKUrls(statusCode: Int, url: String)

object CrawlerJobRun {

  def getNumPagesDownloaded(crawlerJobRunId: Long): Long = {
    DB.withConnection { implicit c =>
      SQL("select COUNT(*) as count from downloads where downloaded=true and crawl_id = {crawlerJobRunId}").on(
        'crawlerJobRunId -> crawlerJobRunId
      ).apply().headOption match {
        case Some(row) => row[Long]("count")
        case None => 0
      }
    }
  }

  def getTotalResponseTime(crawlerJobRunId: Long): Long = {
    DB.withConnection { implicit c =>
      SQL("select SUM(response_time) as sum from downloads where downloaded=true and crawl_id = {crawlerJobRunId}").on(
        'crawlerJobRunId -> crawlerJobRunId
      ).apply().headOption match {
        case Some(row) => {
          row[java.math.BigDecimal]("sum").longValue()
        }
        case None => 0
      }
    }
  }

  def getnon200Urls(crawlerJobRunId: Long): List[NonOKUrls] = {
    DB.withConnection { implicit c =>
      SQL("select http_code, url from downloads where http_code <> 200 and crawl_id = {crawlerJobRunId} order by http_code").on(
        'crawlerJobRunId -> crawlerJobRunId
      ).as(nonOkResults *)
    }
  }

  def allForJob(crawlerJobId: Long): List[CrawlerJobRun] = DB.withConnection { implicit c =>
    DB.withConnection { implicit c =>
      SQL("select * from crawlerjobrun where crawlerJobId = {crawlerJobId}").on(
        'crawlerJobId -> crawlerJobId
      ).as(crawlerjobrun *)
    }
  }

  def getCrawlerJobRun(crawlerJobRunId: Long) : Option[CrawlerJobRun] = {
    DB.withConnection { implicit c =>
      SQL("select * from crawlerjobrun where crawlerJobRunId = {id}").on(
        'id -> crawlerJobRunId
      ).as(crawlerjobrun.singleOpt)
    }
  }


  def create(crawlerJobId : Long ): Option[Long] =  {
    DB.withConnection { implicit c =>
      SQL("insert into crawlerjobrun (crawlerJobId, starttime) values ({crawlerJobId}, {starttime})").on(
        'starttime -> LocalDateTime.now(),
        'crawlerJobId -> crawlerJobId
      ).executeInsert()
    }
  }

  def updateStopTime(crawlerJobId : Long) {
    DB.withConnection { implicit c =>
      SQL("update crawlerjobrun set stoptime = {stoptime} where crawlerJobRunId = {id}").on(
        'id -> crawlerJobId,
        'stoptime -> LocalDateTime.now()
      ).executeUpdate()
    }
  }

  private val crawlerjobrun = {
    get[Pk[Long]]("crawlerJobRunId") ~
    get[Long]("crawlerJobId") ~
    get[LocalDateTime]("starttime") ~
    get[Option[LocalDateTime]]("stoptime") map {
      case crawlerJobRunId~crawlerJobId~starttime~stoptime
        => CrawlerJobRun(crawlerJobRunId, crawlerJobId, starttime, stoptime)
    }
  }

  private val nonOkResults = {
    get[Int]("http_code") ~
    get[String]("url") map {
      case http_code~url
        => NonOKUrls(http_code, url)
    }
  }
}
