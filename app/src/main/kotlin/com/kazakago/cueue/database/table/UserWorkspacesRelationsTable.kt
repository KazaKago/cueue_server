package com.kazakago.cueue.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object UserWorkspacesRelationsTable : LongIdTable() {
    val userId = reference(name = "user_id", foreign = UsersTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val workspaceId = reference(name = "workspace_id", foreign = WorkspacesTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}