package com.kazakago.cooking_planner.domain.model

data class RecipeRegistrationData(
    val title: String,
    val description: String,
    val tagNames: List<TagName>,
)
