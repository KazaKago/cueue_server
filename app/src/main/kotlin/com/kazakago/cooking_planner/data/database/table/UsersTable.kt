package com.kazakago.cooking_planner.data.database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object UsersTable : LongIdTable() {
    val email = text("email").uniqueIndex()
}
