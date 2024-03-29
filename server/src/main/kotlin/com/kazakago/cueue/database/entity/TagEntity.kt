package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.RecipeTagsRelationsTable
import com.kazakago.cueue.database.table.TagsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TagEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TagEntity>(TagsTable)

    var name by TagsTable.name
    var sortOrder by TagsTable.sortOrder
    var createdAt by TagsTable.createdAt
    var updatedAt by TagsTable.updatedAt
    var workspace by WorkspaceEntity referencedOn TagsTable.workspaceId
    var recipes by RecipeEntity via RecipeTagsRelationsTable
}
