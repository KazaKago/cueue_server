package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.InvitationEntity
import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.model.UserId
import com.kazakago.cueue.model.WorkspaceId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class InvitationRepository {

    suspend fun createInvitation(userId: UserId, workspaceId: WorkspaceId, invitationCode: InvitationCode) {
        newSuspendedTransaction {
            InvitationEntity.new {
                this.code = invitationCode.value
                this.createdBy = UserEntity[userId.value]
                this.workspace = WorkspaceEntity[workspaceId.value]
            }
        }
    }
}
