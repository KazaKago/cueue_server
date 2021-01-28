package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.mapper.RecipeMapper
import com.kazakago.cueue.model.Recipe
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.TagName
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.emptySized
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class RecipeRepository(private val recipeMapper: RecipeMapper) {

    suspend fun getRecipes(afterId: RecipeId?, tagName: TagName?): List<Recipe> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val recipes = if (tagName != null) {
                val tag = TagEntity.find { TagsTable.name eq tagName.value }.firstOrNull()
                tag?.recipes ?: emptySized()
            } else {
                RecipeEntity.all()
            }.apply {
                orderBy(RecipesTable.id to SortOrder.DESC)
            }
            val offset = if (afterId != null) {
                recipes.indexOfFirst { it.id.value == afterId.value }.toLong() + 1
            } else {
                0
            }
            recipes.limit(20, offset)
        }.map {
            recipeMapper.toModel(it)
        }
    }

    suspend fun getRecipe(recipeId: RecipeId): Recipe {
        return newSuspendedTransaction(db = DbSettings.db) {
            RecipeEntity[recipeId.value]
        }.let {
            recipeMapper.toModel(it)
        }
    }

    suspend fun createRecipe(recipe: RecipeRegistrationData): Recipe {
        return newSuspendedTransaction(db = DbSettings.db) {
            RecipeEntity.new {
                title = recipe.title
                description = recipe.description
            }.apply {
                val rawTagNames = recipe.tagNames.map { it.value }
                tags = TagEntity.find { TagsTable.name inList rawTagNames }
            }
        }.let {
            recipeMapper.toModel(it)
        }
    }

    suspend fun updateRecipe(recipeId: RecipeId, recipe: RecipeRegistrationData): Recipe {
        return newSuspendedTransaction(db = DbSettings.db) {
            RecipeEntity[recipeId.value].apply {
                title = recipe.title
                description = recipe.description
                updatedAt = LocalDateTime.now()
                val rawTagNames = recipe.tagNames.map { it.value }
                tags = TagEntity.find { TagsTable.name inList rawTagNames }
            }
        }.let {
            recipeMapper.toModel(it)
        }
    }

    suspend fun deleteRecipe(recipeId: RecipeId) {
        newSuspendedTransaction(db = DbSettings.db) {
            val recipe = RecipeEntity[recipeId.value]
            recipe.delete()
        }
    }
}
