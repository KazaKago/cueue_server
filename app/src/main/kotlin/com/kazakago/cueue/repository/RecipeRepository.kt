package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.RecipeUpdatingData
import com.kazakago.cueue.model.TagName
import io.ktor.features.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class RecipeRepository {

    suspend fun getRecipes(workspace: WorkspaceEntity, afterId: RecipeId?, tagName: TagName?): List<RecipeEntity> {
        return newSuspendedTransaction {
            val recipes = if (tagName != null) {
                val tag = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name eq tagName.value) }.firstOrNull() ?: throw MissingRequestParameterException("tag_name (${tagName.value})")
                tag.recipes
            } else {
                RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) }
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
            recipes.limit(20, offset).toList()
        }
    }

    suspend fun getRecipe(workspace: WorkspaceEntity, recipeId: RecipeId): RecipeEntity {
        return newSuspendedTransaction {
            RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id eq recipeId.value) }.first()
        }
    }

    suspend fun createRecipe(workspace: WorkspaceEntity, recipe: RecipeRegistrationData): RecipeEntity {
        return newSuspendedTransaction {
            RecipeEntity.new {
                this.title = recipe.title
                this.description = recipe.description
                this.workspace = workspace
            }.apply {
                val rawTagNames = recipe.tagNames.map { it.value }
                this.tags = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name inList rawTagNames) }
            }
        }
    }

    suspend fun updateRecipe(workspace: WorkspaceEntity, recipeId: RecipeId, recipe: RecipeUpdatingData): RecipeEntity {
        return newSuspendedTransaction {
            RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id eq recipeId.value) }.first().apply {
                recipe.title?.let { this.title = it }
                recipe.description?.let { this.description = it }
                recipe.tagNames?.let { tagNames ->
                    val rawTagNames = tagNames.map { it.value }
                    this.tags = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name inList rawTagNames) }
                }
                this.updatedAt = LocalDateTime.now()
            }
        }
    }

    suspend fun deleteRecipe(workspace: WorkspaceEntity, recipeId: RecipeId) {
        newSuspendedTransaction {
            val recipe = RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id eq recipeId.value) }.first()
            recipe.delete()
        }
    }
}
