package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task
import java.util.Calendar
import java.text.SimpleDateFormat

object Application extends Controller {

  def index = Action {
    Redirect(routes.Application.tasks)
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      x => x match {
        case (label, who) => {
          // Получаем текущее время
          //----------------------------
          Task.create(label, who, getTime())
          Redirect(routes.Application.tasks)
        }
      })
  }

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  def completeTask(id: Long) = Action {
    Task.complete(id)
    Redirect(routes.Application.tasks)
  }

  val taskForm = Form(
    tuple(
      "label" -> nonEmptyText,
      "who" -> nonEmptyText))
  private def getTime(): String = {
    val today = Calendar.getInstance().getTimeInMillis()
    val timeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
    val time = timeFormat.format(today)
    time
  }

}
