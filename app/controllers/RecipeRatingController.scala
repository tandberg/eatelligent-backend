package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.JsonFormats
import play.api.libs.json._
import play.api.mvc._
import repository.daos.RatingDAO
import repository.models._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future


class RecipeRatingController @Inject() (
                                   val ratingDAO: RatingDAO,
                                   implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def listRatingsForUser = SecuredAction.async { implicit request =>
    val ratings = ratingDAO.findStarRatingsForUser(request.identity.userID.toString)
    ratings.map(r => Ok(Json.obj("ok" -> true, "ratings" -> r)))
  }

  def listRatingsForRecipe(recipeId: Long) = SecuredAction.async { implicit request =>
    val ratings = ratingDAO.findStarRatingsForRecipe(recipeId)
    ratings.map(r => Ok(Json.obj("ok" -> true, "ratings" -> r)))
  }

  def saveStarRatingRecipe = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    val ratingResult = request.body.validate[UserStarRateRecipe]
    ratingResult.fold(
      errors => {
        Future {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      rating => {
        val newRating = ratingDAO.saveUserStarRateRecipe(rating)
        newRating.map(r => Created(Json.obj("ok" -> true, "rating" -> r)))
      }
    )
  }

}
