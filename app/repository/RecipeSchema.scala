package models

import java.sql.Date

import myUtils.WithMyDriver
import play.api.libs.json.{JsValue, Json}

case class RecipeSchema(
                  id: Option[Long],
                  name: String,
                  image: Option[JsValue],
                  description: String,
                  language: Int,
                  calories: Double,
                  procedure: String,
                  created: Date,
                  modified: Date
                  )

case class Recipe(
                   id: Option[Long],
                   name: String,
                   image: Option[JsValue],
                   description: String,
                   language: Int,
                   calories: Double,
                   procedure: String,
                   created: Date,
                   modified: Date,
                   ingredients: Seq[IngredientForRecipe]
                   )

trait RecipeComponent extends WithMyDriver with IngredientComponent {
  import driver.simple._

  class Recipes(tag: Tag) extends Table[RecipeSchema](tag, "recipe") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[JsValue]]("image")
    def description = column[String]("description")
    def language = column[Int]("language")
    def calories = column[Double]("calories")
    def procedure = column[String]("procedure")
    def created = column[Date]("created")
    def modified = column[Date]("modified")

    def * = (id, name, image, description, language, calories, procedure, created, modified) <> (RecipeSchema.tupled,
      RecipeSchema.unapply)
  }

  val recipes = TableQuery[Recipes]

  private val recipesAutoInc = recipes returning recipes.map(_.id) into {
    case (r, id) => r.copy(id = id)
  }

  def ins(recipe: RecipeSchema)(implicit session: Session): Long = {
    (recipes returning recipes.map(_.id) += recipe).toList.head
  }

  def insert(recipe: RecipeSchema)(implicit session: Session): RecipeSchema =
    recipesAutoInc.insert(recipe)

  def saveRecipeToDb(r: Recipe)(implicit session: Session): Recipe = {
    session.withTransaction{
      val rid = ins(RecipeSchema(r.id, r.name, r.image, r.description, r.language, r.calories, r.procedure,
        r.created, r.modified))
      r.ingredients.foreach{
        i =>
          if (i.ingredientId == None) {
            val iid: Long = insertIngredient(IngredientSchema(None, i.name, i.image))
            ingredientsInRecipe.insert(IngredientInRecipeSchema(rid, iid, i.amount))
          } else {
            ingredientsInRecipe.insert(IngredientInRecipeSchema(rid, i.ingredientId.toList.head, i.amount))
          }
      }
    }
    r
  }

  def findRecipeById(id: Long)(implicit session: Session): Option[Recipe] = {
    val recipe = recipes.filter(_.id === id).list.headOption
    val i = findIngredientsForRecipe(id)
    recipe match {
      case Some(r) => Some(Recipe(r.id, r.name, r.image, r.description, r.language, r.calories, r.procedure,
        r.created,
        r.modified, i))
      case None => None
    }
  }

}
