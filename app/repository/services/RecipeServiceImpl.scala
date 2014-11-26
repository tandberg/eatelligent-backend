package repository.services

import java.io.File

import com.google.inject.Inject
import repository.{RecipeImage, TinyRecipe, Recipe}
import repository.daos.RecipeDAO

import scala.concurrent.Future

class RecipeServiceImpl @Inject() (recipeDAO: RecipeDAO) extends RecipeService {

  def save(recipe: Recipe): Future[Option[Recipe]] = {
    recipeDAO.save(recipe)
  }

  def find(id: Long): Future[Option[Recipe]] = recipeDAO.find(id)

  def find(name: String): Future[List[TinyRecipe]] = recipeDAO.find(name)

  def getAll: Future[Seq[TinyRecipe]] = recipeDAO.getAll

  def saveImage(id: Long, image: File): Future[RecipeImage] = recipeDAO.saveImage(id, image)
}
