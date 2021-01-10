package com.kazakago.cooking_planner.data.database.entity

import com.kazakago.cooking_planner.data.database.table.RecipeTagsRelationsTable
import com.kazakago.cooking_planner.data.database.table.TagsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TagEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TagEntity>(TagsTable)

    var name by TagsTable.name
    var recipes by RecipeEntity via RecipeTagsRelationsTable
}
