package com.kazakago.cooking_planner.data.database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object RecipesTable : LongIdTable() {
    val title = text("title")
    val description = text("description")
}
