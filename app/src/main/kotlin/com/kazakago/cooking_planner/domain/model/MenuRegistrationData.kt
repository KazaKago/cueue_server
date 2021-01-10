package com.kazakago.cooking_planner.domain.model

import java.time.LocalDateTime

data class MenuRegistrationData(
    val memo: String,
    val date: LocalDateTime,
    val timeFrame: TimeFrame,
    val recipeIds: List<RecipeId>,
)
