package controllers

import play.api.mvc._
import models.CrawlerJob

import play.api.data._
import play.api.data.Forms._
import anorm.{Pk, NotAssigned}

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(CrawlerJob.all()))
  }

  def newJob = Action {
    Ok(views.html.crawlerjob.create(crawlerJobForm))
  }

  def createNewJob =  Action { implicit request =>
    crawlerJobForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.crawlerjob.create(formWithErrors)),
       crawlerJob => {
        val newJobId: Option[Long] = CrawlerJob.create(crawlerJob)
        newJobId match {
          case Some(id) => Redirect(routes.Application.getJob(id))
          case None => InternalServerError(views.html.crawlerjob.create(crawlerJobForm))
        }
      }
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
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "label" -> nonEmptyText,
      "startUrls" -> nonEmptyText
    )(CrawlerJob.apply)(CrawlerJob.unapply)
  )
}