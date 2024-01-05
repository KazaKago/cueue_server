package com.kazakago.cueue.database.table

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object InvitationsTable : LongIdTable() {
    val code = text("code")
    val createdBy = reference(name = "created_by", foreign = UsersTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val workspaceId = reference(name = "workspace_id", foreign = WorkspacesTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }
    val updatedAt = datetime("updated_at").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }
}
