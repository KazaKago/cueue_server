package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.InvitationsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InvitationEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<InvitationEntity>(InvitationsTable)

    var code by InvitationsTable.code
    var createdBy by UserEntity referencedOn InvitationsTable.createdBy
    var workspace by WorkspaceEntity referencedOn InvitationsTable.workspaceId
    var createdAt by InvitationsTable.createdAt
    var updatedAt by InvitationsTable.updatedAt
}
