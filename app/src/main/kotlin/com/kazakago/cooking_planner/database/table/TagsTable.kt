package com.kazakago.cooking_planner.database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object TagsTable : LongIdTable() {
    val name = text("name")
}
