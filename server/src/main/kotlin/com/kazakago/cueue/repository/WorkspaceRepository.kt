package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.mapper.WorkspaceMapper
import com.kazakago.cueue.model.Workspace
import com.kazakago.cueue.model.WorkspaceId
import com.kazakago.cueue.model.WorkspaceRegistrationData
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class WorkspaceRepository(private val workspaceMapper: WorkspaceMapper) {

    suspend fun createWorkspace(workspaceRegistrationData: WorkspaceRegistrationData): Workspace {
        return newSuspendedTransaction {
            val entity = WorkspaceEntity.new {
                this.name = workspaceRegistrationData.name
            }
            workspaceMapper.toModel(entity)
        }
    }

    suspend fun updateWorkspace(workspaceId: WorkspaceId, workspaceRegistrationData: WorkspaceRegistrationData): Workspace {
        return newSuspendedTransaction {
            val entity = WorkspaceEntity[workspaceId.value].apply {
                this.name = workspaceRegistrationData.name
                this.updatedAt = LocalDateTime.now()
            }
            workspaceMapper.toModel(entity)
        }
    }
}
