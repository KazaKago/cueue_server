package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.WorkspacesTable
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class WorkspaceRepository {

    suspend fun createPersonalWorkspace(user: UserEntity): WorkspaceEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            WorkspaceEntity.new {
                this.name = WorkspacesTable.PERSONAL_DEFAULT_NAME
                this.isPersonal = true
            }.apply {
                this.users = SizedCollection(user)
            }
        }
    }
}
