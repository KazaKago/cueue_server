package com.kazakago.cooking_planner.database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object RecipesTable : LongIdTable() {
    val title = text("title")
    val description = text("description")
}
