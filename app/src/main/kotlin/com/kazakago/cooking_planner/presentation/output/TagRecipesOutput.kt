package com.kazakago.cooking_planner.presentation.output

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagRecipesOutput(
    @SerialName("name")
    val name: String,
    @SerialName("recipes")
    val recipes: List<RecipeOutput>,
)
