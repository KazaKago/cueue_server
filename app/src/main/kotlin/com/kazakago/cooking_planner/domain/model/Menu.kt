package com.kazakago.cooking_planner.domain.model

import java.time.LocalDateTime

interface Menu {
    val id: MenuId
    val memo: String
    val date: LocalDateTime
    val timeFrame: TimeFrame
}
