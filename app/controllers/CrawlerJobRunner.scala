package controllers

import play.api.mvc.{Action, Controller}
import models.{CrawlerJobRun, CrawlerJob}
import no.bekk.bekkopen.mightycrawler.{Configuration, Crawler}
import org.slf4j.LoggerFactory

import play.api.libs.concurrent.Execution.Implicits.defaultContext

object CrawlerJobRunner extends Controller {

  private var runningCrawlerJobs : Map[Long, Crawler]= Map()
  private val logger = LoggerFactory.getLogger(getClass)

  def runJob(id: Long) = Action {
    CrawlerJob.getJob(id).map{
      crawlerJob => {
        logger.debug("Trying to run job")
        scala.concurrent.Future {
          logger.debug("In the future")
          val crawlerJobRunId: Option[Long] = CrawlerJobRun.create(id)
          crawlerJobRunId match {
            case Some(crawlerJobId) => {
              try {
                logger.info("Running CrawlerJobRun {}", crawlerJobId)
                val config = new Configuration(CrawlerJob.getConfigurationFile(id).getAbsolutePath)
                config.crawlerId = crawlerJobId
                val crawler: Crawler = new Crawler(config)
                runningCrawlerJobs = runningCrawlerJobs + (id -> crawler)

                crawler.start()
                logger.info("Finished CrawlerJobRun {}", crawlerJobId)
                runningCrawlerJobs = runningCrawlerJobs - id
                CrawlerJobRun.updateStopTime(crawlerJobId)
              } catch {
                case t: Throwable => logger.error("Could not run CrawlerJob", t)
              }
            }
            case None => logger.error("Did not get crawlerJobId")

          }
          logger.debug("After future")
        }
        Redirect(routes.CrawlerJobCRUD.getJob(id))
        //Ok(views.html.crawlerjob.view(crawlerJob, CrawlerJobRun.allForJob(id)))
      }
    }.getOrElse(NotFound)
  }
}
