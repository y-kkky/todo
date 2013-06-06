package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String, who: String, mytime: String, ready: Short)

object Task {

  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }

  def create(label: String, who: String, mytime: String) {
    DB.withConnection { implicit c =>
      SQL("insert into task (label,who,mytime,ready) values ({label},{who},{mytime}, 0)").on(
        'label -> label,
        'who -> who,
        'mytime -> mytime
      ).executeUpdate()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  def complete(id: Long) {
    DB.withConnection { implicit c =>
      SQL("update task set ready=1 where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  val task = {
    get[Long]("id") ~
      get[String]("label") ~
      get[String]("who") ~
      get[String]("mytime") ~
      get[Short]("ready") map {
      case id~label~who~mytime~ready => Task(id, label, who, mytime, ready)
    }
  }

}