package com.kazakago.cooking_planner.database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object UsersTable : LongIdTable() {
    val email = text("email").uniqueIndex()
}
