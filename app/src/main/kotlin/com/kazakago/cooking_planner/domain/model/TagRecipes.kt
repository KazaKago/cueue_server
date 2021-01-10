package com.kazakago.cooking_planner.domain.model

data class TagRecipes(
    override val name: TagName,
    val recipes: List<Recipe>,
) : Tag
