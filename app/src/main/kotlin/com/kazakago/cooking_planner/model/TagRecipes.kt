package com.kazakago.cooking_planner.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagRecipes(
    @SerialName("name")
    val name: TagName,
    @SerialName("recipes")
    val recipes: List<Recipe>,
)