package com.kazakago.cooking_planner.domain.model

interface Recipe {
    val id: RecipeId
    val title: String
    val description: String
}
