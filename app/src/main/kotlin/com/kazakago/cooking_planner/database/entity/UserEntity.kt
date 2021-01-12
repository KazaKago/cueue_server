package com.kazakago.cooking_planner.database.entity

import com.kazakago.cooking_planner.database.table.UsersTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
}
