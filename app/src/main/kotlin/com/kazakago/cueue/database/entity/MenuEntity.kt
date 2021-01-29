package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.MenuRecipesRelationsTable
import com.kazakago.cueue.database.table.MenusTable
import org.jetbrains.exposed.dao.EntityBatchUpdate
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class MenuEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MenuEntity>(MenusTable)

    var memo by MenusTable.memo
    var date by MenusTable.date
    var timeFrame by MenusTable.timeFrame
    var createdAt by MenusTable.createdAt
    var updatedAt by MenusTable.updatedAt
    var workspace by WorkspaceEntity referencedOn MenusTable.workspaceId
    var recipes by RecipeEntity via MenuRecipesRelationsTable

    override fun flush(batch: EntityBatchUpdate?): Boolean {
        updatedAt = LocalDateTime.now()
        return super.flush(batch)
    }
}
