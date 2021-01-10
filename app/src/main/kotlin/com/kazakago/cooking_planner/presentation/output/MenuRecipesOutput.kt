package com.kazakago.cooking_planner.presentation.output

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuRecipesOutput(
    @SerialName("id")
    val id: String,
    @SerialName("memo")
    val memo: String,
    @SerialName("date")
    val date: String,
    @SerialName("time_frame")
    val timeFrame: String,
    @SerialName("recipes")
    val recipes: List<RecipeOutput>,
)
