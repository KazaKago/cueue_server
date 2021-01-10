package com.kazakago.cooking_planner.presentation.output

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagOutput(
    @SerialName("name")
    val name: String,
)
