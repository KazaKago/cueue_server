package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.model.Workspace
import com.kazakago.cueue.model.WorkspaceId

class WorkspaceMapper(private val userSummaryMapper: UserSummaryMapper) {

    fun toModel(workspace: WorkspaceEntity): Workspace {
        return Workspace(
            id = WorkspaceId(workspace.id.value),
            users = workspace.users
                .sortedBy { it.id }
                .map { userSummaryMapper.toModel(it) }
        )
    }
}
