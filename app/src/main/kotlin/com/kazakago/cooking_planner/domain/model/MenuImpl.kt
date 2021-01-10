package com.kazakago.cooking_planner.domain.model

import java.time.LocalDateTime

data class MenuImpl(
    override val id: MenuId,
    override val memo: String,
    override val date: LocalDateTime,
    override val timeFrame: TimeFrame,
) : Menu
