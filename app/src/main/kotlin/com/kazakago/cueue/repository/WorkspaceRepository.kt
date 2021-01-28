package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.UsersTable
import com.kazakago.cueue.mapper.WorkspaceMapper
import com.kazakago.cueue.model.UserId
import com.kazakago.cueue.model.Workspace
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class WorkspaceRepository(private val workspaceMapper: WorkspaceMapper) {

    suspend fun createWorkspace(userId: UserId): Workspace {
        return newSuspendedTransaction(db = DbSettings.db) {
            WorkspaceEntity.new {
            }.apply {
                users = UserEntity.find { UsersTable.id eq userId.value }
            }
        }.let {
            workspaceMapper.toModel(it)
        }
    }
}
