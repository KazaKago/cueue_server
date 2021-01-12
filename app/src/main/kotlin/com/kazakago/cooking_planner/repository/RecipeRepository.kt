package com.kazakago.cooking_planner.repository

import com.kazakago.cooking_planner.database.entity.RecipeEntity
import com.kazakago.cooking_planner.database.entity.TagEntity
import com.kazakago.cooking_planner.database.setting.DbSettings
import com.kazakago.cooking_planner.database.table.TagsTable
import com.kazakago.cooking_planner.mapper.RecipeTagsMapper
import com.kazakago.cooking_planner.model.RecipeId
import com.kazakago.cooking_planner.model.RecipeRegistrationData
import com.kazakago.cooking_planner.model.RecipeTags
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeRepository(private val recipeTagsMapper: RecipeTagsMapper) {

    suspend fun getRecipeTagsList(): List<RecipeTags> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val recipes = RecipeEntity.all()
            recipes.map { recipeTagsMapper.toModel(it) }
        }
    }

    suspend fun getRecipeTags(recipeId: RecipeId): RecipeTags {
        return newSuspendedTransaction(db = DbSettings.db) {
            val recipe = RecipeEntity.findById(recipeId.value) ?: throw NoSuchElementException()
            recipeTagsMapper.toModel(recipe)
        }
    }

    suspend fun createRecipe(recipe: RecipeRegistrationData) {
        newSuspendedTransaction(db = DbSettings.db) {
            RecipeEntity.new {
                title = recipe.title
                description = recipe.description
            }.apply {
                val rawTagNames = recipe.tagNames.map { it.value }
                tags = TagEntity.find { TagsTable.name inList rawTagNames }
            }
        }
    }

    suspend fun deleteRecipe(recipeId: RecipeId) {
        newSuspendedTransaction(db = DbSettings.db) {
            val recipe = RecipeEntity.findById(recipeId.value) ?: throw NoSuchElementException()
            recipe.delete()
        }
    }
}
