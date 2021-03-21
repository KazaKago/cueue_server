package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.ContentEntity
import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.ContentsTable
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.mapper.RecipeMapper
import com.kazakago.cueue.model.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class RecipeRepository(private val recipeMapper: RecipeMapper) {

    suspend fun getRecipes(workspaceId: WorkspaceId, afterId: RecipeId?, tagId: TagId?): List<Recipe> {
        return newSuspendedTransaction {
            val recipes = if (tagId != null) {
                val tag = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id eq tagId.value) }.firstOrNull() ?: throw MissingRequestParameterException("tag_id (${tagId.value})")
                tag.recipes
            } else {
                RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) }
            }.apply {
                orderBy(RecipesTable.id to SortOrder.DESC)
            }
            val offset = if (afterId != null) {
                val index = recipes.indexOfFirst { it.id.value == afterId.value }
                if (index < 0) throw MissingRequestParameterException("after_id (${afterId.value})")
                index + 1L
            } else {
                0L
            }
            val entities = recipes.limit(20, offset).toList()
            entities.map { recipeMapper.toModel(it) }
        }
    }

    suspend fun getRecipe(workspaceId: WorkspaceId, recipeId: RecipeId): Recipe {
        return newSuspendedTransaction {
            val entity = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id eq recipeId.value) }.first()
            recipeMapper.toModel(entity)
        }
    }

    suspend fun createRecipe(workspaceId: WorkspaceId, recipe: RecipeRegistrationData): Recipe {
        return newSuspendedTransaction {
            val entity = RecipeEntity.new {
                this.title = recipe.title
                this.description = recipe.description ?: ""
                this.url = recipe.url
                this.workspace = WorkspaceEntity[workspaceId.value]
            }.apply {
                recipe.imageKeys?.map { imageKey ->
                    ContentEntity.find { ContentsTable.key eq imageKey }.map { it.recipe = this }
                }
                val rawTagIds = recipe.tagIds?.map { it.value } ?: emptyList()
                this.tags = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id inList rawTagIds) }
            }
            recipeMapper.toModel(entity)
        }
    }

    suspend fun updateRecipe(workspaceId: WorkspaceId, recipeId: RecipeId, recipe: RecipeUpdatingData): Recipe {
        return newSuspendedTransaction {
            val entity = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id eq recipeId.value) }.first().apply {
                this.title = recipe.title
                this.description = recipe.description ?: ""
                this.url = recipe.url
                this.images.map { it.recipe = null }
                recipe.imageKeys?.map { imageKey ->
                    ContentEntity.find { ContentsTable.key eq imageKey }.map { it.recipe = this }
                }
                val rawTagIds = recipe.tagIds?.map { it.value } ?: emptyList()
                this.tags = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id inList rawTagIds) }
                this.updatedAt = LocalDateTime.now()
            }
            recipeMapper.toModel(entity)
        }
    }

    suspend fun deleteRecipe(workspaceId: WorkspaceId, recipeId: RecipeId) {
        newSuspendedTransaction {
            val recipe = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id eq recipeId.value) }.first()
            recipe.delete()
        }
    }
}
