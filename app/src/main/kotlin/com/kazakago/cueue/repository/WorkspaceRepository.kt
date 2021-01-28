package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.setting.DbSettings
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class WorkspaceRepository {

    suspend fun createWorkspace(user: UserEntity): WorkspaceEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            WorkspaceEntity.new {}.apply {
                this.users = SizedCollection(user)
            }
        }
    }
}
