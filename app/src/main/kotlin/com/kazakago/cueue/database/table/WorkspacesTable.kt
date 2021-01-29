package com.kazakago.cueue.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object WorkspacesTable : LongIdTable() {
    const val PERSONAL_DEFAULT_NAME = "personal"

    val name = text("name")
    val isPersonal = bool("is_personal")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}
