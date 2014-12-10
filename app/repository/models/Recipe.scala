package repository.models

import org.joda.time.LocalDateTime

case class Recipe(
                   id: Option[Long],
                   name: String,
                   image: Option[String],
                   description: Option[String],
                   language: Int,
                   calories: Option[Double],
                   procedure: Option[String],
                   spicy: Option[Int],
                   time: Option[Int],
                   created: Option[LocalDateTime],
                   modified: Option[LocalDateTime],
                   published: Option[LocalDateTime],
                   deleted: Option[LocalDateTime],
                   ingredients: Seq[IngredientForRecipe],
                   tags: Seq[String],
                   createdBy: Option[TinyUser]
                   )
