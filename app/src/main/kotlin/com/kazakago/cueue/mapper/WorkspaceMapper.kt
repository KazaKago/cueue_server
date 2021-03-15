package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.model.Workspace
import com.kazakago.cueue.model.WorkspaceId
import com.kazakago.cueue.model.WorkspaceType

class WorkspaceMapper {

    fun toModel(workspace: WorkspaceEntity): Workspace {
        return Workspace(
            id = WorkspaceId(workspace.id.value),
            name = workspace.name,
            type = WorkspaceType.resolve(workspace.type)
        )
    }
}
