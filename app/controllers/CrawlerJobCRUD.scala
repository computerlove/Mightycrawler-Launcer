package controllers

import play.api.mvc._
import models.CrawlerJob

import play.api.data._
import play.api.data.Forms._
import anorm.{Pk, NotAssigned}

object CrawlerJobCRUD extends Controller {

  def index = Action {
    Ok(views.html.index(CrawlerJob.all()))
  }

  def newJob = Action {
    Ok(views.html.crawlerjob.create(crawlerJobForm.fill(new CrawlerJob(label = "", startUrls = ""))))
  }

  def editJob(id: Long) = Action {
    CrawlerJob.getJob(id).map{
      crawlerJob => Ok(views.html.crawlerjob.edit(id, crawlerJobForm.fill(crawlerJob)))
    }.getOrElse(NotFound)
  }

  def createNewJob =  Action { implicit request =>
    crawlerJobForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.crawlerjob.create(formWithErrors)),
      crawlerJob => {
        val newJobId: Option[Long] = CrawlerJob.create(crawlerJob)
        newJobId match {
          case Some(id) => Redirect(routes.CrawlerJobCRUD.getJob(id))
          case None => InternalServerError(views.html.crawlerjob.create(crawlerJobForm))
        }
      }
    )
  }

  def updateJob(id: Long) =  Action { implicit request =>
    crawlerJobForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.crawlerjob.create(formWithErrors)),
      crawlerJob => {
        CrawlerJob.update(id, crawlerJob)
        Redirect(routes.CrawlerJobCRUD.getJob(id))
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
    Redirect(routes.CrawlerJobCRUD.index)
  }

  val crawlerJobForm = Form(
    mapping(
      "id" -> ignored(NotAssigned:Pk[Long]),
      "label" -> nonEmptyText,
      "startUrls" -> nonEmptyText,
      "extractPattern" -> nonEmptyText,
      "linkPattern" -> nonEmptyText,
      "storePattern" -> text,
      "userAgent" -> nonEmptyText,
      "downloadThreads" -> number,
      "maxVisits" -> number,
      "maxDownloads" -> number,
      "maxRecursion" -> number,
      "maxTime" -> number,
      "downloadDelay" -> number,
      "responseTimeout" -> number,
      "crawlerTimeout" -> number
    )(CrawlerJob.apply)(CrawlerJob.unapply)
  )
}