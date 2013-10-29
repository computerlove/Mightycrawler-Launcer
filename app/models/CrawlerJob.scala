package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.io.{FileOutputStream, File}
import play.api.Play
import java.util.Properties

case class CrawlerJob(id: Pk[Long] = NotAssigned,
                      label: String,
                      startUrls: String,
                      extractPattern: String = "text/html",
                      linkPattern: String = "href=\"(.*)\"",
                      storePattern: String = "",
                      userAgent: String = "MightyCrawlerBot",
                      downloadThreads: Int = 10,
                      maxVisits: Int = 100,
                      maxDownloads: Int = 500,
                      maxRecursion: Int = 5,
                      maxTime: Int = 300,
                      downloadDelay: Int = 1,
                      responseTimeout: Int = 30,
                      crawlerTimeout: Int = 30)

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
      SQL("insert into crawlerjob (label, startUrls, extractPattern, linkPattern, storePattern, userAgent, downloadThreads, maxVisits, maxDownloads, maxRecursion, maxTime, downloadDelay, responseTimeout, crawlerTimeout) values ({label}, {startUrls},{extractPattern}, {linkPattern}, {storePattern}, {userAgent}, {downloadThreads}, {maxVisits}, {maxDownloads}, {maxRecursion}, {maxTime}, {downloadDelay}, {responseTimeout}, {crawlerTimeout})").on(
        'label -> crawlerJob.label,
        'startUrls -> crawlerJob.startUrls,
        'extractPattern -> crawlerJob.extractPattern,
        'linkPattern -> crawlerJob.linkPattern,
        'storePattern -> crawlerJob.storePattern,
        'userAgent -> crawlerJob.userAgent,
        'downloadThreads -> crawlerJob.downloadThreads,
        'maxVisits -> crawlerJob.maxVisits,
        'maxDownloads -> crawlerJob.maxDownloads,
        'maxRecursion -> crawlerJob.maxRecursion,
        'maxTime -> crawlerJob.maxTime,
        'downloadDelay -> crawlerJob.downloadDelay,
        'responseTimeout -> crawlerJob.responseTimeout,
        'crawlerTimeout -> crawlerJob.crawlerTimeout
      ).executeInsert()
    }
  }

  def update(id: Long, crawlerJob: CrawlerJob)  {
    DB.withConnection { implicit c =>
      SQL("update crawlerjob set label={label}, startUrls={startUrls}, extractPattern={extractPattern}, linkPattern={linkPattern}, storePattern={storePattern}, userAgent={userAgent}, downloadThreads={downloadThreads}, maxVisits={maxVisits}, maxDownloads={maxDownloads}, maxRecursion={maxRecursion}, maxTime={maxTime}, downloadDelay={downloadDelay}, responseTimeout={responseTimeout}, crawlerTimeout={crawlerTimeout}").on(
        'label -> crawlerJob.label,
        'startUrls -> crawlerJob.startUrls,
        'extractPattern -> crawlerJob.extractPattern,
        'linkPattern -> crawlerJob.linkPattern,
        'storePattern -> crawlerJob.storePattern,
        'userAgent -> crawlerJob.userAgent,
        'downloadThreads -> crawlerJob.downloadThreads,
        'maxVisits -> crawlerJob.maxVisits,
        'maxDownloads -> crawlerJob.maxDownloads,
        'maxRecursion -> crawlerJob.maxRecursion,
        'maxTime -> crawlerJob.maxTime,
        'downloadDelay -> crawlerJob.downloadDelay,
        'responseTimeout -> crawlerJob.responseTimeout,
        'crawlerTimeout -> crawlerJob.crawlerTimeout
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
      get[String]("startUrls") ~
      get[String]("extractPattern") ~
      get[String]("linkPattern") ~
      get[String]("storePattern") ~
      get[String]("userAgent") ~
      get[Int]("downloadThreads") ~
      get[Int]("maxVisits") ~
      get[Int]("maxDownloads") ~
      get[Int]("maxRecursion") ~
      get[Int]("maxTime") ~
      get[Int]("downloadDelay") ~
      get[Int]("responseTimeout") ~
      get[Int]("crawlerTimeout") map {
      case id~label~startUrls~extractPattern~linkPattern~storePattern~userAgent~downloadThreads~maxVisits~maxDownloads~maxRecursion~maxTime~downloadDelay~responseTimeout~crawlerTimeout => CrawlerJob(id, label, startUrls, extractPattern, linkPattern, storePattern, userAgent, downloadThreads, maxVisits, maxDownloads, maxRecursion, maxTime, downloadDelay, responseTimeout, crawlerTimeout)
    }
  }

  def saveToConfig(crawlerJob: CrawlerJob) {
    val properties: Properties = new Properties
    properties.put("startUrls", crawlerJob.startUrls)
    properties.put("extractPattern", crawlerJob.extractPattern)
    properties.put("linkPattern", crawlerJob.linkPattern)
    properties.put("storePattern", crawlerJob.storePattern)
    properties.put("userAgent", crawlerJob.userAgent)
    properties.put("downloadThreads", crawlerJob.downloadThreads.toString)
    properties.put("maxVisits", crawlerJob.maxVisits.toString)
    properties.put("maxDownloads", crawlerJob.maxDownloads.toString)
    properties.put("maxRecursion", crawlerJob.maxRecursion.toString)
    properties.put("maxTime", crawlerJob.maxTime.toString)
    properties.put("downloadDelay", crawlerJob.downloadDelay.toString)
    properties.put("responseTimeout", crawlerJob.responseTimeout.toString)
    properties.put("crawlerTimeout", crawlerJob.crawlerTimeout.toString)
    val file: File = getConfigurationFile(crawlerJob.id.get)

    val stream: FileOutputStream = new FileOutputStream(file)
    properties.store(stream, "")
    scala.reflect.io.File.closeQuietly(stream)
  }

  def getConfigurationFile(id: Long): File = {
    new File(Play.configuration.getString("appDir").get, "/" + id + "/config.properties")
  }
}