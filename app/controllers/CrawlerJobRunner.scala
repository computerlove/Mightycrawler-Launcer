package controllers

import play.api.mvc.{Action, Controller}
import models.CrawlerJob

object CrawlerJobRunner extends Controller {
  def runJob(id: Long) = Action {
    CrawlerJob.getJob(id).map{
      crawlerJob => {
        no.bekk.bekkopen.mightycrawler.Configuration
        Ok(views.html.crawlerjob.view(crawlerJob))
      }
    }.getOrElse(NotFound)
  }
}
