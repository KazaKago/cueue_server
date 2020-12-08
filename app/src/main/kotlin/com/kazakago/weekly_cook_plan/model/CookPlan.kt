package com.kazakago.weekly_cook_plan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CookPlan(
    @SerialName("recipe")
    val recipe: String,
)
