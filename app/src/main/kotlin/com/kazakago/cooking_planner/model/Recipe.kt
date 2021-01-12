package com.kazakago.cooking_planner.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    @SerialName("id")
    val id: RecipeId,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
)
