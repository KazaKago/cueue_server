package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.ContentEntity
import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.ContentsTable
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.mapper.RecipeMapper
import com.kazakago.cueue.mapper.RecipeSummaryMapper
import com.kazakago.cueue.model.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class RecipeRepository(private val recipeMapper: RecipeMapper, private val recipeSummaryMapper: RecipeSummaryMapper) {

    suspend fun getRecipes(workspaceId: WorkspaceId, afterId: RecipeId?, tagId: TagId?): List<RecipeSummary> {
        return newSuspendedTransaction {
            val recipes = if (tagId != null) {
                val tag = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id eq tagId.value) }.firstOrNull() ?: throw MissingRequestParameterException("tag_id (${tagId.value})")
                tag.recipes
            } else {
                RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) }
            }.apply {
                orderBy(RecipesTable.id to SortOrder.DESC)
            }
            val offset = recipes.getOffset(afterId?.value)
            recipes.limit(20, offset).map { recipeSummaryMapper.toModel(it) }
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
                this.createImageRelations(recipe.imageKeys)
                val rawTagIds = recipe.tagIds?.map { it.value } ?: emptyList()
                this.tags = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id inList rawTagIds) }
            }
            recipeMapper.toModel(entity)
        }
    }

    suspend fun updateRecipe(workspaceId: WorkspaceId, recipeId: RecipeId, recipe: RecipeRegistrationData): Recipe {
        return newSuspendedTransaction {
            val entity = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id eq recipeId.value) }.first().apply {
                this.title = recipe.title
                this.description = recipe.description ?: ""
                this.url = recipe.url
                this.clearImageRelations()
                this.createImageRelations(recipe.imageKeys)
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

    private fun RecipeEntity.clearImageRelations() {
        images.map {
            it.recipe = null
            it.recipeOrder = 0
        }
    }

    private fun RecipeEntity.createImageRelations(imageKeys: List<String>?) {
        imageKeys?.mapIndexed { index, imageKey ->
            ContentEntity.find { ContentsTable.key eq imageKey }.map {
                it.recipe = this
                it.recipeOrder = index
            }
        }
    }
}
