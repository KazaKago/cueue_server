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
import com.kazakago.cueue.model.Recipe
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.RecipeSummary
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.WorkspaceId
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.ColumnSet
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeRepository(private val recipeMapper: RecipeMapper, private val recipeSummaryMapper: RecipeSummaryMapper) {

    suspend fun getRecipes(workspaceId: WorkspaceId, keyword: String?, tagIds: List<TagId>?): List<RecipeSummary> {
        return newSuspendedTransaction {
            var table: ColumnSet = RecipesTable
            var conditions = RecipesTable.workspaceId eq workspaceId.value
            if (!keyword.isNullOrBlank()) {
                conditions = conditions and ((RecipesTable.title like "%$keyword%") or (RecipesTable.hiragana like "%$keyword%") or (RecipesTable.katakana like "%$keyword%"))
            }
            if (!tagIds.isNullOrEmpty()) {
                lateinit var tagIdsCondition: Op<Boolean>
                tagIds.forEachIndexed { index, tagId ->
                    tagIdsCondition = if (index == 0) {
                        RecipeTagsRelationsTable.tagId eq tagId.value
                    } else {
                        tagIdsCondition or (RecipeTagsRelationsTable.tagId eq tagId.value)
                    }
                }
                table = table.leftJoin(RecipeTagsRelationsTable)
                conditions = conditions and tagIdsCondition
            }
            val query = table
                .select(RecipesTable.columns)
                .where(conditions)
                .withDistinct()
                .orderBy(RecipesTable.id to SortOrder.DESC)
            RecipeEntity.wrapRows(query)
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
                    this.hiragana = recipe.furigana.hiragana
                    this.katakana = recipe.furigana.katakana
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
                    this.hiragana = recipe.furigana.hiragana
                    this.katakana = recipe.furigana.katakana
                    this.description = recipe.description ?: ""
                    this.url = recipe.url
                    this.clearImageRelations()
                    this.createImageRelations(recipe.imageKeys)
                    val rawTagIds = recipe.tagIds?.map { it.value } ?: emptyList()
                    this.tags = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id inList rawTagIds) }
                    this.updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC)
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
