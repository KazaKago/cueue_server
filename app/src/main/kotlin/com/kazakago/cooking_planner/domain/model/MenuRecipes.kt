package com.kazakago.cooking_planner.domain.model

import java.time.LocalDateTime

data class MenuRecipes(
    override val id: MenuId,
    override val memo: String,
    override val date: LocalDateTime,
    override val timeFrame: TimeFrame,
    val recipes: List<Recipe>,
) : Menu
