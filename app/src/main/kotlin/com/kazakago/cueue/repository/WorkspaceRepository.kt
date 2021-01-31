package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.WorkspacesTable
import com.kazakago.cueue.mapper.rawValue
import com.kazakago.cueue.model.WorkspaceType
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class WorkspaceRepository {

    suspend fun createPersonalWorkspace(user: UserEntity): WorkspaceEntity {
        return newSuspendedTransaction {
            WorkspaceEntity.new {
                this.name = WorkspacesTable.PERSONAL_DEFAULT_NAME
                this.type = WorkspaceType.Personal.rawValue()
            }.apply {
                this.users = SizedCollection(user)
            }
        }
    }
}
