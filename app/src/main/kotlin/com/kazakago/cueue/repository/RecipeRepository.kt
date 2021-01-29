package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.TagName
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.emptySized
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeRepository {

    suspend fun getRecipes(workspace: WorkspaceEntity, afterId: RecipeId?, tagName: TagName?): List<RecipeEntity> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val recipes = if (tagName != null) {
                val tag = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name eq tagName.value) }.firstOrNull()
                tag?.recipes ?: emptySized()
            } else {
                RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) }
            }.apply {
                orderBy(RecipesTable.id to SortOrder.DESC)
            }
            val offset = if (afterId != null) {
                recipes.indexOfFirst { it.id.value == afterId.value }.toLong() + 1
            } else {
                0
            }
            recipes.limit(20, offset).toList()
        }
    }

    suspend fun getRecipe(workspace: WorkspaceEntity, recipeId: RecipeId): RecipeEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id eq recipeId.value) }.first()
        }
    }

    suspend fun createRecipe(workspace: WorkspaceEntity, recipe: RecipeRegistrationData): RecipeEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
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

    suspend fun updateRecipe(workspace: WorkspaceEntity, recipeId: RecipeId, recipe: RecipeRegistrationData): RecipeEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id eq recipeId.value) }.first().apply {
                this.title = recipe.title
                this.description = recipe.description
                this.workspace = workspace
                val rawTagNames = recipe.tagNames.map { it.value }
                this.tags = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name inList rawTagNames) }
            }
        }
    }

    suspend fun deleteRecipe(workspace: WorkspaceEntity, recipeId: RecipeId) {
        newSuspendedTransaction(db = DbSettings.db) {
            val recipe = RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id eq recipeId.value) }.first()
            recipe.delete()
        }
    }
}
