package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class CrawlerJob(id: Pk[Long] = NotAssigned, label: String, startUrls: String)

object CrawlerJob {

  def all(): List[CrawlerJob] = DB.withConnection { implicit c =>
    SQL("select * from crawlerjob").as(crawlerjob *)
  }

  def getJob(id: Long) : Option[CrawlerJob] = {
    DB.withConnection { implicit c =>
      SQL("select * from crawlerjob where id = {id}").on(
        'id -> id
      ).as(crawlerjob.singleOpt)
    }
  }

  def create(crawlerJob: CrawlerJob): Option[Long] =  {
    DB.withConnection { implicit c =>
      SQL("insert into crawlerjob (label, startUrls) values ({label}, {startUrls})").on(
        'label -> crawlerJob.label,
        'startUrls -> crawlerJob.startUrls
      ).executeInsert()
    }
  }

  def update(id: Long, crawlerJob: CrawlerJob)  {
    DB.withConnection { implicit c =>
      SQL("update crawlerjob set label={label}, startUrls={startUrls}").on(
        'label -> crawlerJob.label,
        'startUrls -> crawlerJob.startUrls
      ).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from crawlerjob where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  val crawlerjob = {
    get[Pk[Long]]("id") ~
      get[String]("label") ~
        get[String]("startUrls") map {
        case id~label~startUrls => CrawlerJob(id, label, startUrls)
    }
  }
}