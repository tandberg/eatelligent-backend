package controllers

import models.User
import repository.{Role, MyUser}
import play.api.db.slick.DBAction
import play.api.mvc.Controller
import play.api.libs.functional.syntax._
import play.api.libs.json._
import repository.current.dao._
import repository.current.dao.driver.simple._
import play.api.db.slick._
import play.api.mvc._
import play.api.Play.current

object UserController extends Controller {
  
  implicit val userRead: Reads[MyUser] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "image").readNullable[JsValue] and
      (JsPath \ "city").readNullable[String] and
        (JsPath \ "role").read[Long]
    )(MyUser.apply _)

  implicit val userWrites: Writes[MyUser] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \"image").write[Option[JsValue]] and
      (JsPath \ "city").write[Option[String]] and
        (JsPath \ "role").write[Long]
    )(unlift(MyUser.unapply))

  implicit val roleRead: Reads[Role] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String]
    )(Role.apply _)

  implicit val roleWrite: Writes[Role] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "name").write[String]
    )(unlift(Role.unapply))

//  def listUsers = DBAction { implicit request =>
//    val json = Json.toJson(users.list)
//    Ok(Json.obj("ok" -> true, "users" -> json))
//  }
//
//
//  def getUser(id: Long) = DBAction { implicit session =>
//    val user = findUserById(id)
//    val role = user match {
//      case Some(u) => findRoleById(u.roleId)
//      case None => None
//    }
//    val userJson = Json.toJson(user)
//    val roleJson = Json.toJson(role)
//    Ok(Json.obj("ok" -> true, "user" -> userJson, "role" -> role))
//  }

}
