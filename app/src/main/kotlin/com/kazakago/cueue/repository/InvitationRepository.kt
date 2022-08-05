package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.InvitationEntity
import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.InvitationsTable
import com.kazakago.cueue.mapper.InvitationMapper
import com.kazakago.cueue.model.Invitation
import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.model.UserId
import com.kazakago.cueue.model.WorkspaceId
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class InvitationRepository(private val invitationMapper: InvitationMapper) {

    suspend fun getInvitation(invitationCode: InvitationCode): Invitation {
        return newSuspendedTransaction {
            val entity = InvitationEntity
                .find { (InvitationsTable.code eq invitationCode.value) and (InvitationsTable.isConsumed eq false) and (InvitationsTable.createdAt greater LocalDateTime.now().minusDays(1)) }
                .first()
            invitationMapper.toModel(entity)
        }
    }

    suspend fun createInvitation(userId: UserId, workspaceId: WorkspaceId, invitationCode: InvitationCode): Invitation {
        return newSuspendedTransaction {
            val entity = InvitationEntity.new {
                this.code = invitationCode.value
                this.createdBy = UserEntity[userId.value]
                this.workspace = WorkspaceEntity[workspaceId.value]
            }
            invitationMapper.toModel(entity)
        }
    }
}
