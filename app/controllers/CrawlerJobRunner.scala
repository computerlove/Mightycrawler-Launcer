package controllers

import play.api.mvc.{Action, Controller}
import models.CrawlerJob
import no.bekk.bekkopen.mightycrawler.Crawler

object CrawlerJobRunner extends Controller {
  def runJob(id: Long) = Action {
    CrawlerJob.getJob(id).map{
      crawlerJob => {
        scala.concurrent.Future {
          val crawler: Crawler = new Crawler()
          crawler.init(CrawlerJob.getConfigurationFile(id).getAbsolutePath)
        }
        Ok(views.html.crawlerjob.view(crawlerJob))
      }
    }.getOrElse(NotFound)
  }
}
