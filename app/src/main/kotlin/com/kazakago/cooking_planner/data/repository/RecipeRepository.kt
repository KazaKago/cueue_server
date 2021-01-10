package com.kazakago.cooking_planner.data.repository

import com.kazakago.cooking_planner.data.database.entity.RecipeEntity
import com.kazakago.cooking_planner.data.database.entity.TagEntity
import com.kazakago.cooking_planner.data.database.setting.DbSettings
import com.kazakago.cooking_planner.data.database.table.TagsTable
import com.kazakago.cooking_planner.data.mapper.RecipeTagsMapper
import com.kazakago.cooking_planner.domain.model.RecipeId
import com.kazakago.cooking_planner.domain.model.RecipeRegistrationData
import com.kazakago.cooking_planner.domain.model.RecipeTags
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
