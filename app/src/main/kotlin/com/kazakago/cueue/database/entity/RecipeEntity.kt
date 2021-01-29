package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.MenuRecipesRelationsTable
import com.kazakago.cueue.database.table.RecipeTagsRelationsTable
import com.kazakago.cueue.database.table.RecipesTable
import org.jetbrains.exposed.dao.EntityBatchUpdate
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class RecipeEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RecipeEntity>(RecipesTable)

    var title by RecipesTable.title
    var description by RecipesTable.description
    var createdAt by RecipesTable.createdAt
    var updatedAt by RecipesTable.updatedAt
    var workspace by WorkspaceEntity referencedOn RecipesTable.workspaceId
    var tags by TagEntity via RecipeTagsRelationsTable
    var menus by MenuEntity via MenuRecipesRelationsTable

    override fun flush(batch: EntityBatchUpdate?): Boolean {
        updatedAt = LocalDateTime.now()
        return super.flush(batch)
    }
}
