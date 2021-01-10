package com.kazakago.cooking_planner.data.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object MenusTable : LongIdTable() {
    val memo = text("memo")
    val date = datetime("description")
    val timeFrame = text("time_frame")
}
