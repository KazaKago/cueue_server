package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.UserWorkspacesRelationsTable
import com.kazakago.cueue.database.table.UsersTable
import org.jetbrains.exposed.dao.EntityBatchUpdate
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UsersTable)

    var uid by UsersTable.uid
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
    var workspaces by WorkspaceEntity via UserWorkspacesRelationsTable

    override fun flush(batch: EntityBatchUpdate?): Boolean {
        updatedAt = LocalDateTime.now()
        return super.flush(batch)
    }

    suspend fun personalWorkSpace(): WorkspaceEntity {
        return newSuspendedTransaction {
            workspaces.first { it.isPersonal }
        }
    }
}
