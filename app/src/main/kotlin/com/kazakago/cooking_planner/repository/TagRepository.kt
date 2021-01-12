package com.kazakago.cooking_planner.repository

import com.kazakago.cooking_planner.database.entity.TagEntity
import com.kazakago.cooking_planner.database.setting.DbSettings
import com.kazakago.cooking_planner.database.table.TagsTable
import com.kazakago.cooking_planner.exception.EntityDuplicateException
import com.kazakago.cooking_planner.mapper.TagMapper
import com.kazakago.cooking_planner.model.Tag
import com.kazakago.cooking_planner.model.TagName
import com.kazakago.cooking_planner.model.TagRegistrationData
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TagRepository(private val tagMapper: TagMapper) {

    suspend fun getTags(): List<Tag> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val tag = TagEntity.all()
            tag.map { tagMapper.toModel(it) }
        }
    }

    suspend fun getTag(tagName: TagName): Tag {
        return newSuspendedTransaction(db = DbSettings.db) {
            val tag = TagEntity.find { TagsTable.name eq tagName.value }.first()
            tagMapper.toModel(tag)
        }
    }

    suspend fun createTag(tag: TagRegistrationData) {
        newSuspendedTransaction(db = DbSettings.db) {
            val existingTag = TagEntity.find { TagsTable.name eq tag.name.value }
            if (!existingTag.empty()) throw EntityDuplicateException()
            TagEntity.new {
                name = tag.name.value
            }
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
