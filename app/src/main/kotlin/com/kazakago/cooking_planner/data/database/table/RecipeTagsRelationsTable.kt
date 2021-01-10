package com.kazakago.cooking_planner.data.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object RecipeTagsRelationsTable : LongIdTable() {
    val recipeId = reference(name = "recipe_id", foreign = RecipesTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val tagId = reference(name = "tag_id", foreign = TagsTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
}
