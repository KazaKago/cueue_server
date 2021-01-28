package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.exception.EntityDuplicateException
import com.kazakago.cueue.model.TagName
import com.kazakago.cueue.model.TagRegistrationData
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class TagRepository {

    suspend fun getTags(workspace: WorkspaceEntity): List<TagEntity> {
        return newSuspendedTransaction(db = DbSettings.db) {
            TagEntity.find { TagsTable.workspaceId eq workspace.id.value }.toList()
        }
    }

    suspend fun getTag(workspace: WorkspaceEntity, tagName: TagName): TagEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name eq tagName.value) }.first()
        }
    }

    suspend fun createTag(workspace: WorkspaceEntity, tag: TagRegistrationData): TagEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            val existingTag = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name eq tag.name.value) }
            if (!existingTag.empty()) throw EntityDuplicateException()
            TagEntity.new {
                this.name = tag.name.value
                this.workspace = workspace
            }
        }
    }

    suspend fun updateTag(workspace: WorkspaceEntity, tagName: TagName, tag: TagRegistrationData): TagEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name eq tagName.value) }.first().apply {
                this.name = tag.name.value
                this.workspace = workspace
                this.updatedAt = LocalDateTime.now()
            }
        }
    }

    suspend fun deleteTag(workspace: WorkspaceEntity, tagName: TagName) {
        newSuspendedTransaction(db = DbSettings.db) {
            val tags = TagEntity.find { (TagsTable.workspaceId eq workspace.id.value) and (TagsTable.name eq tagName.value) }
            if (tags.empty()) throw NoSuchElementException()
            tags.map { it.delete() }
        }
    }
}
