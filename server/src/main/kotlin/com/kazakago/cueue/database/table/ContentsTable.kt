package com.kazakago.cueue.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object ContentsTable : LongIdTable() {
    val key = text("key")
    val recipeId = optReference(name = "recipe_id", foreign = RecipesTable, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.SET_NULL)
    val recipeOrder = integer("recipe_order").default(0)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val updatedAt = datetime("updated_at").clientDefault { LocalDateTime.now() }
}
