package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.mapper.TagMapper
import com.kazakago.cueue.model.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class TagRepository(private val tagMapper: TagMapper) {

    suspend fun getTags(workspaceId: WorkspaceId): List<Tag> {
        return newSuspendedTransaction {
            val entities = TagEntity.find { TagsTable.workspaceId eq workspaceId.value }
                .orderBy(TagsTable.sortOrder to SortOrder.ASC)
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
                this.sortOrder = TagEntity.count(TagsTable.workspaceId eq workspaceId.value)
                this.workspace = WorkspaceEntity[workspaceId.value]
            }
            tagMapper.toModel(entity)
        }
    }

    suspend fun updateTags(workspaceId: WorkspaceId, tags: TagSortRegistrationData): List<Tag> {
        return newSuspendedTransaction {
            tags.tagIds.mapIndexed { index, tagId ->
                val entity = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id eq tagId.value) }.first().apply {
                    this.sortOrder = index.toLong()
                    this.updatedAt = LocalDateTime.now()
                }
                tagMapper.toModel(entity)
            }
        }
    }

    suspend fun updateTag(workspaceId: WorkspaceId, tagId: TagId, tag: TagRegistrationData): Tag {
        return newSuspendedTransaction {
            val entity = TagEntity.find { (TagsTable.workspaceId eq workspaceId.value) and (TagsTable.id eq tagId.value) }.first().apply {
                this.name = tag.name
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
