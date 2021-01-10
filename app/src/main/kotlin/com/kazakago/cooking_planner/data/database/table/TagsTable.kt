package com.kazakago.cooking_planner.data.database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object TagsTable : LongIdTable() {
    val name = text("name")
}
