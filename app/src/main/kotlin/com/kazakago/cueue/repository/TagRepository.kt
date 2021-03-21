package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.mapper.TagMapper
import com.kazakago.cueue.model.Tag
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.model.WorkspaceId
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class TagRepository(private val tagMapper: TagMapper) {

    suspend fun getTags(workspaceId: WorkspaceId): List<Tag> {
        return newSuspendedTransaction {
            val entities = TagEntity.find { TagsTable.workspaceId eq workspaceId.value }
                .orderBy(TagsTable.id to SortOrder.ASC)
                .toList()
            entities.map { tagMapper.toModel(it) }
        }
    }

    suspend fun getTag(workspaceId: WorkspaceId, tagId: TagId): Tag {
        return newSuspendedTransaction {
            val entity = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id eq tagId.value) }.first()
            tagMapper.toModel(entity)
        }
    }

    suspend fun createTag(workspaceId: WorkspaceId, tag: TagRegistrationData): Tag {
        return newSuspendedTransaction {
            val entity = TagEntity.new {
                this.name = tag.name
                this.workspace = WorkspaceEntity[workspaceId.value]
            }.apply {
                val rawRecipeIds = tag.recipeIds?.map { it.value } ?: emptyList()
                this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id inList rawRecipeIds) }
            }
            tagMapper.toModel(entity)
        }
    }

    suspend fun updateTag(workspaceId: WorkspaceId, tagId: TagId, tag: TagRegistrationData): Tag {
        return newSuspendedTransaction {
            val entity = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id eq tagId.value) }.first().apply {
                this.name = tag.name
                val rawRecipeIds = tag.recipeIds?.map { it.value } ?: emptyList()
                this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id inList rawRecipeIds) }
                this.updatedAt = LocalDateTime.now()
            }
            tagMapper.toModel(entity)
        }
    }

    suspend fun deleteTag(workspaceId: WorkspaceId, tagId: TagId) {
        newSuspendedTransaction {
            val tags = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id eq tagId.value) }
            if (tags.empty()) throw NoSuchElementException()
            tags.map { it.delete() }
        }
    }
}
