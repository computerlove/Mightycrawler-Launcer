package controllers

import play.api.mvc._
import models.CrawlerJob

import play.api.data._
import play.api.data.Forms._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(CrawlerJob.all(), crawlerJobForm))
  }

  def newJob =  Action { implicit request =>
    crawlerJobForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(CrawlerJob.all(), errors)),
    {case (label, startUrls) => {
        val newJobId: Option[Long] = CrawlerJob.create(label, startUrls)
        newJobId match {
          case Some(id) => Redirect(routes.Application.getJob(id))
          case None => InternalServerError(views.html.index(CrawlerJob.all(),
            crawlerJobForm.withError(new FormError("Error when inserting CrawlerJob, no id returned", ""))))
        }
      }}
    )
  }

  def getJob(id: Long) = Action {
    CrawlerJob.getJob(id) match {
      case Some(job) => Ok(views.html.crawlerjob.view(job))
      case None => NotFound
    }
  }

  def deleteJob(id: Long) = Action {
    CrawlerJob.delete(id)
    Redirect(routes.Application.index)
  }

  val crawlerJobForm = Form(
    tuple("label" -> nonEmptyText, "startUrls" -> nonEmptyText)
  )
}