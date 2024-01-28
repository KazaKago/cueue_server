package com.kazakago.cueue.database.table

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UsersTable : LongIdTable() {
    val uid = text("uid").uniqueIndex()
    val displayName = text("display_name")
    val photoId = optReference(name = "photo_id", foreign = ContentsTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.SET_NULL)
    val workspaceId = reference(name = "workspace_id", foreign = WorkspacesTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.SET_NULL)
    val createdAt = datetime("created_at").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }
    val updatedAt = datetime("updated_at").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }
}
