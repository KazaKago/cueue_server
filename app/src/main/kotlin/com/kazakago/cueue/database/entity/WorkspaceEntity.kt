package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.*
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class WorkspaceEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<WorkspaceEntity>(WorkspacesTable)

    var name by WorkspacesTable.name
    var createdAt by WorkspacesTable.createdAt
    var updatedAt by WorkspacesTable.updatedAt
    val recipes by RecipeEntity referrersOn RecipesTable.workspaceId
    val tags by TagEntity referrersOn TagsTable.workspaceId
    val menus by MenuEntity referrersOn MenusTable.workspaceId
    var users by UserEntity via UserWorkspacesRelationsTable

    fun isDefault(): Boolean {
        return name == WorkspacesTable.DEFAULT_NAME
    }
}
