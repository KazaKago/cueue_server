package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.ContentEntity
import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.ContentsTable
import com.kazakago.cueue.database.table.RecipeTagsRelationsTable
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.mapper.RecipeMapper
import com.kazakago.cueue.mapper.RecipeSummaryMapper
import com.kazakago.cueue.model.*
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class RecipeRepository(private val recipeMapper: RecipeMapper, private val recipeSummaryMapper: RecipeSummaryMapper) {

    suspend fun getRecipes(workspaceId: WorkspaceId, afterId: RecipeId?, tagId: TagId?, title: String?): List<RecipeSummary> {
        return newSuspendedTransaction {
            var table: ColumnSet = RecipesTable
            var conditions = RecipesTable.workspaceId eq workspaceId.value
            if (tagId != null) {
                table = table.leftJoin(RecipeTagsRelationsTable)
                conditions = conditions and (RecipeTagsRelationsTable.tagId eq tagId.value)
            }
            if (title != null) {
                conditions = conditions and ((RecipesTable.title like "%$title%") or (RecipesTable.kana like "%$title%"))
            }
            val query = table
                .slice(RecipesTable.columns)
                .select(conditions)
                .orderBy(RecipesTable.id to SortOrder.DESC)
            val recipes = RecipeEntity.wrapRows(query)
            val offset = recipes.getOffset(afterId?.value)
            recipes
                .limit(20, offset)
                .with(RecipeEntity::images, RecipeEntity::menus)
                .map { recipeSummaryMapper.toModel(it) }
        }
    }

    suspend fun getRecipe(workspaceId: WorkspaceId, recipeId: RecipeId): Recipe {
        return newSuspendedTransaction {
            val entity = RecipeEntity
                .find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id eq recipeId.value) }
                .first()
                .load(RecipeEntity::images, RecipeEntity::menus, RecipeEntity::tags)
            recipeMapper.toModel(entity)
        }
    }

    suspend fun createRecipe(workspaceId: WorkspaceId, recipe: RecipeRegistrationData): Recipe {
        return newSuspendedTransaction {
            val entity = RecipeEntity
                .new {
                    this.title = recipe.title
                    this.kana = recipe.kana
                    this.description = recipe.description ?: ""
                    this.url = recipe.url
                    this.workspace = WorkspaceEntity[workspaceId.value]
                }
                .apply {
                    this.createImageRelations(recipe.imageKeys)
                    val rawTagIds = recipe.tagIds?.map { it.value } ?: emptyList()
                    this.tags = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id inList rawTagIds) }
                }
            recipeMapper.toModel(entity)
        }
    }

    suspend fun updateRecipe(workspaceId: WorkspaceId, recipeId: RecipeId, recipe: RecipeRegistrationData): Recipe {
        return newSuspendedTransaction {
            val entity = RecipeEntity
                .find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id eq recipeId.value) }
                .first()
                .load(RecipeEntity::images, RecipeEntity::menus, RecipeEntity::tags)
                .apply {
                    this.title = recipe.title
                    this.kana = recipe.kana
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
            RecipeEntity
                .find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id eq recipeId.value) }
                .first()
                .delete()
        }
    }

    private fun RecipeEntity.clearImageRelations() {
        ContentEntity
            .find { ContentsTable.recipeId eq id }
            .map {
                it.recipe = null
                it.recipeOrder = 0
            }
    }

    private fun RecipeEntity.createImageRelations(imageKeys: List<String>?) {
        imageKeys?.mapIndexed { index, imageKey ->
            ContentEntity
                .find { ContentsTable.key eq imageKey }
                .map {
                    it.recipe = this
                    it.recipeOrder = index
                }
        }
    }
}
