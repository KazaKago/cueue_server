package com.kazakago.cueue.database.table

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object MenuRecipesRelationsTable : LongIdTable() {
    val menuId = reference(name = "menu_id", foreign = MenusTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val recipeId = reference(name = "recipe_id", foreign = RecipesTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }
    val updatedAt = datetime("updated_at").clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }
}
