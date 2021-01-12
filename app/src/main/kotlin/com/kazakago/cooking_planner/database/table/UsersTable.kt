package com.kazakago.cooking_planner.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object UsersTable : LongIdTable() {
    val email = text("email").uniqueIndex()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}
