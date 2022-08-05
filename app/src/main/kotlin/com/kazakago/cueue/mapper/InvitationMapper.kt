package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.InvitationEntity
import com.kazakago.cueue.model.Invitation
import com.kazakago.cueue.model.InvitationCode

class InvitationMapper(private val workspaceMapper: WorkspaceMapper) {

    fun toModel(invitation: InvitationEntity): Invitation {
        return Invitation(
            code = InvitationCode(invitation.code),
            workspace = workspaceMapper.toModel(invitation.workspace),
        )
    }
}
