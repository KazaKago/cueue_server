package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.RecipesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RecipeSummaryEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RecipeSummaryEntity>(RecipesTable)

    var title by RecipesTable.title
    var description by RecipesTable.description
}
