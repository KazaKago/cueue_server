package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.UsersTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UsersTable)

    var uid by UsersTable.uid
    var workspace by WorkspaceEntity optionalReferencedOn UsersTable.workspaceId
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
}
