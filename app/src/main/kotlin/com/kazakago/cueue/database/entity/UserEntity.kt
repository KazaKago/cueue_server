package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.UserWorkspacesRelationsTable
import com.kazakago.cueue.database.table.UsersTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UsersTable)

    var uid by UsersTable.uid
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
    var workspaces by WorkspaceEntity via UserWorkspacesRelationsTable

    suspend fun defaultWorkSpace(): WorkspaceEntity {
        return newSuspendedTransaction {
            workspaces.firstOrNull { it.isDefault() } ?: workspaces.first()
        }
    }
}
