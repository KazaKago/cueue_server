package com.kazakago.cueue.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UsersTable : LongIdTable() {
    val uid = text("uid").uniqueIndex()
    val workspaceId = optReference(name = "workspace_id", foreign = WorkspacesTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.SET_NULL)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}
