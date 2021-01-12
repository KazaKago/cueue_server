package com.kazakago.cooking_planner.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object MenuRecipesRelationsTable : LongIdTable() {
    val menuId = reference(name = "menu_id", foreign = MenusTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val recipeId = reference(name = "recipe_id", foreign = RecipesTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}
