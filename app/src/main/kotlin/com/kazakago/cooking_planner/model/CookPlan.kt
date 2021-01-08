package com.kazakago.cooking_planner.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookPlan(
    @SerialName("recipe")
    val recipe: String,
)
