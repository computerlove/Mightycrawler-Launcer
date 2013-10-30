package controllers

import play.api.mvc.{Action, Controller}
import models.{CrawlerJobRun, CrawlerJob}
import no.bekk.bekkopen.mightycrawler.{Configuration, Crawler}
import org.slf4j.LoggerFactory

import play.api.libs.concurrent.Execution.Implicits.defaultContext

object CrawlerJobRunner extends Controller {

  private var runningCrawlerJobs : Map[Long, Crawler]= Map()

  def runJob(id: Long) = Action {
    CrawlerJob.getJob(id).map{
      crawlerJob => {
        scala.concurrent.Future {
          CrawlerJobRun.create(id) match {
            case Some(crawlerJobId) => {
              val config = new Configuration(CrawlerJob.getConfigurationFile(id).getAbsolutePath)
              config.crawlerId = crawlerJobId
              val crawler: Crawler = new Crawler(config)
              runningCrawlerJobs = runningCrawlerJobs +  (id -> crawler)

              crawler.start()

              runningCrawlerJobs = runningCrawlerJobs - id
              CrawlerJobRun.updateStopTime(crawlerJobId)
            }
            case None => LoggerFactory.getLogger(getClass).error("Did not get crawlerJobId")

          }

        }
        Ok(views.html.crawlerjob.view(crawlerJob))
      }
    }.getOrElse(NotFound)
  }
}
