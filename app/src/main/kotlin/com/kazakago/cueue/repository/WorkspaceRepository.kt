package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.UserWorkspacesRelationsTable
import com.kazakago.cueue.database.table.WorkspacesTable
import com.kazakago.cueue.mapper.WorkspaceMapper
import com.kazakago.cueue.model.UserId
import com.kazakago.cueue.model.Workspace
import com.kazakago.cueue.model.WorkspaceId
import com.kazakago.cueue.model.WorkspaceRegistrationData
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class WorkspaceRepository(private val workspaceMapper: WorkspaceMapper) {

    suspend fun getWorkspaces(userId: UserId): List<Workspace> {
        return newSuspendedTransaction {
            val table = WorkspacesTable.leftJoin(UserWorkspacesRelationsTable)
            val conditions = UserWorkspacesRelationsTable.userId eq userId.value
            val query = table
                .slice(WorkspacesTable.columns)
                .select(conditions)
                .withDistinct()
            val entities = WorkspaceEntity.wrapRows(query)
            entities.map { workspaceMapper.toModel(it) }
        }
    }

    suspend fun getWorkspace(workspaceId: WorkspaceId): Workspace {
        return newSuspendedTransaction {
            val table = WorkspacesTable.leftJoin(UserWorkspacesRelationsTable)
            val conditions = WorkspacesTable.id eq workspaceId.value
            val query = table
                .slice(WorkspacesTable.columns)
                .select(conditions)
                .first()
            val entity = WorkspaceEntity.wrapRow(query)
            workspaceMapper.toModel(entity)
        }
    }

    suspend fun createWorkspace(userId: UserId, workspaceRegistrationData: WorkspaceRegistrationData): Workspace {
        return newSuspendedTransaction {
            val entity = WorkspaceEntity.new {
                this.name = workspaceRegistrationData.name
                this.users = SizedCollection(UserEntity[userId.value])
            }
            workspaceMapper.toModel(entity)
        }
    }

    suspend fun updateWorkspace(workspaceId: WorkspaceId, workspaceRegistrationData: WorkspaceRegistrationData): Workspace {
        return newSuspendedTransaction {
            val table = WorkspacesTable.leftJoin(UserWorkspacesRelationsTable)
            val conditions = WorkspacesTable.id eq workspaceId.value
            val query = table
                .slice(WorkspacesTable.columns)
                .select(conditions)
                .first()
            val entity = WorkspaceEntity.wrapRow(query)
                .apply {
                    this.name = workspaceRegistrationData.name
                    this.updatedAt = LocalDateTime.now()
                }
            workspaceMapper.toModel(entity)
        }
    }

    suspend fun deleteWorkspace(workspaceId: WorkspaceId) {
        newSuspendedTransaction {
            val table = WorkspacesTable.leftJoin(UserWorkspacesRelationsTable)
            val conditions = WorkspacesTable.id eq workspaceId.value
            val query = table
                .slice(WorkspacesTable.columns)
                .select(conditions)
                .first()
            WorkspaceEntity.wrapRow(query)
                .delete()
        }
    }
}
