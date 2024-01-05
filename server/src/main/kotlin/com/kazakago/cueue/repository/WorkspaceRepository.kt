package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.mapper.WorkspaceMapper
import com.kazakago.cueue.model.Workspace
import com.kazakago.cueue.model.WorkspaceId
import com.kazakago.cueue.model.WorkspaceRegistrationData
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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
                this.updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC)
            }
            workspaceMapper.toModel(entity)
        }
    }
}
