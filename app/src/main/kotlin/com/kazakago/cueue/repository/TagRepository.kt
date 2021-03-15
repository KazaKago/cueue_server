package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.model.TagUpdatingData
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class TagRepository {

    suspend fun getTags(workspace: WorkspaceEntity): List<TagEntity> {
        return newSuspendedTransaction {
            TagEntity.find { TagsTable.workspaceId eq workspace.id.value }
                .orderBy(TagsTable.id to SortOrder.ASC)
                .toList()
        }
    }

    suspend fun getTag(workspace: WorkspaceEntity, tagId: TagId): TagEntity {
        return newSuspendedTransaction {
            TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.id eq tagId.value) }.first()
        }
    }

    suspend fun createTag(workspace: WorkspaceEntity, tag: TagRegistrationData): TagEntity {
        return newSuspendedTransaction {
            TagEntity.new {
                this.name = tag.name
                this.workspace = workspace
            }.apply {
                val rawRecipeIds = tag.recipeIds?.map { it.value } ?: emptyList()
                this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id inList rawRecipeIds) }
            }
        }
    }

    suspend fun updateTag(workspace: WorkspaceEntity, tagId: TagId, tag: TagUpdatingData): TagEntity {
        return newSuspendedTransaction {
            TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.id eq tagId.value) }.first().apply {
                this.name = tag.name
                val rawRecipeIds = tag.recipeIds?.map { it.value } ?: emptyList()
                this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id inList rawRecipeIds) }
                this.updatedAt = LocalDateTime.now()
            }
        }
    }

    suspend fun deleteTag(workspace: WorkspaceEntity, tagId: TagId) {
        newSuspendedTransaction {
            val tags = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.id eq tagId.value) }
            if (tags.empty()) throw NoSuchElementException()
            tags.map { it.delete() }
        }
    }
}
