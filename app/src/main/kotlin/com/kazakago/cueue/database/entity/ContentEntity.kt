package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.ContentsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ContentEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ContentEntity>(ContentsTable)

    var key by ContentsTable.key
    var recipe by RecipeEntity optionalReferencedOn ContentsTable.recipeId
}
