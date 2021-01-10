package com.kazakago.cooking_planner.domain.model

data class RecipeImpl(
    override val id: RecipeId,
    override val title: String,
    override val description: String,
) : Recipe
