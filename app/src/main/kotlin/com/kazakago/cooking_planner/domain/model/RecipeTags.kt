package com.kazakago.cooking_planner.domain.model

data class RecipeTags(
    override val id: RecipeId,
    override val title: String,
    override val description: String,
    val tagNames: List<TagName>,
) : Recipe
