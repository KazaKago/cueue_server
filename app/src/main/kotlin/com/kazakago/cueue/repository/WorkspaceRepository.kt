package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.WorkspacesTable
import com.kazakago.cueue.mapper.WorkspaceMapper
import com.kazakago.cueue.mapper.rawValue
import com.kazakago.cueue.model.UserId
import com.kazakago.cueue.model.Workspace
import com.kazakago.cueue.model.WorkspaceType
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class WorkspaceRepository(private val workspaceMapper: WorkspaceMapper) {

    suspend fun createPersonalWorkspace(userId: UserId): Workspace {
        return newSuspendedTransaction {
            val entity = WorkspaceEntity.new {
                this.name = WorkspacesTable.PERSONAL_DEFAULT_NAME
                this.type = WorkspaceType.Personal.rawValue()
            }.apply {
                this.users = SizedCollection(UserEntity[userId.value])
            }
            workspaceMapper.toModel(entity)
        }
    }
}
