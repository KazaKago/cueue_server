package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.UserWorkspacesRelationsTable
import com.kazakago.cueue.database.table.WorkspacesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class WorkspaceEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<WorkspaceEntity>(WorkspacesTable)

    var name by WorkspacesTable.name
    var createdAt by WorkspacesTable.createdAt
    var updatedAt by WorkspacesTable.updatedAt
    var users by UserEntity via UserWorkspacesRelationsTable
}
