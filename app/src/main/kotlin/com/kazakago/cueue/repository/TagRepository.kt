package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.TagsTable
import com.kazakago.cueue.exception.EntityDuplicateException
import com.kazakago.cueue.mapper.TagMapper
import com.kazakago.cueue.model.Tag
import com.kazakago.cueue.model.TagName
import com.kazakago.cueue.model.TagRegistrationData
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class TagRepository(private val tagMapper: TagMapper) {

    suspend fun getTags(): List<Tag> {
        return newSuspendedTransaction(db = DbSettings.db) {
            TagEntity.all()
        }.let {
            it.map { tag -> tagMapper.toModel(tag) }
        }
    }

    suspend fun getTag(tagName: TagName): Tag {
        return newSuspendedTransaction(db = DbSettings.db) {
            TagEntity.find { TagsTable.name eq tagName.value }.first()
        }.let {
            tagMapper.toModel(it)
        }
    }

    suspend fun createTag(tag: TagRegistrationData): Tag {
        return newSuspendedTransaction(db = DbSettings.db) {
            val existingTag = TagEntity.find { TagsTable.name eq tag.name.value }
            if (!existingTag.empty()) throw EntityDuplicateException()
            TagEntity.new {
                name = tag.name.value
            }
        }.let {
            tagMapper.toModel(it)
        }
    }

    suspend fun updateTag(tagName: TagName, tag: TagRegistrationData) {
        return newSuspendedTransaction(db = DbSettings.db) {
            TagEntity.find { TagsTable.name eq tagName.value }.first().apply {
                name = tag.name.value
                updatedAt = LocalDateTime.now()
            }
        }.let {
            tagMapper.toModel(it)
        }
    }

    suspend fun deleteTag(tagName: TagName) {
        newSuspendedTransaction(db = DbSettings.db) {
            val tags = TagEntity.find { TagsTable.name eq tagName.value }
            if (tags.empty()) throw NoSuchElementException()
            tags.map { it.delete() }
        }
    }
}
