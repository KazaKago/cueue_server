package com.kazakago.cueue.repository

import com.google.firebase.cloud.StorageClient
import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.RecipeUpdatingData
import com.kazakago.cueue.model.TagId
import io.ktor.features.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class RecipeRepository {

    suspend fun getRecipes(workspace: WorkspaceEntity, afterId: RecipeId?, tagId: TagId?): List<RecipeEntity> {
        return newSuspendedTransaction {
            val recipes = if (tagId != null) {
                val tag = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.id eq tagId.value) }.firstOrNull() ?: throw MissingRequestParameterException("tag_id (${tagId.value})")
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
                this.description = recipe.description ?: ""
                this.image = recipe.decodedImage?.let {
                    val bucket = StorageClient.getInstance().bucket()
                    val blob = bucket.create(it.createFilePath(workspace), it.imageByte, it.mimeType)
                    blob.name
                }
                this.workspace = workspace
            }.apply {
                val rawTagIds = recipe.tagIds?.map { it.value } ?: emptyList()
                this.tags = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.id inList rawTagIds) }
            }
        }
    }

    suspend fun updateRecipe(workspace: WorkspaceEntity, recipeId: RecipeId, recipe: RecipeUpdatingData): RecipeEntity {
        return newSuspendedTransaction {
            RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id eq recipeId.value) }.first().apply {
                recipe.title?.let { title = it }
                recipe.description?.let { description = it }
                recipe.decodedImage?.let {
                    val bucket = StorageClient.getInstance().bucket()
                    if (image != null) {
                        bucket.get(image).delete()
                    }
                    val blob = bucket.create(it.createFilePath(workspace), it.imageByte, it.mimeType)
                    image = blob.name
                }
                recipe.tagIds?.let { tagIds ->
                    val rawTagIds = tagIds.map { it.value }
                    tags = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.id inList rawTagIds) }
                }
                updatedAt = LocalDateTime.now()
            }
        }
    }

    suspend fun deleteRecipe(workspace: WorkspaceEntity, recipeId: RecipeId) {
        newSuspendedTransaction {
            val recipe = RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id eq recipeId.value) }.first()
            if (recipe.image != null) {
                val bucket = StorageClient.getInstance().bucket()
                bucket.get(recipe.image).delete()
            }
            recipe.delete()
        }
    }
}
