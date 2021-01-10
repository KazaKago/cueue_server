package com.kazakago.cooking_planner.presentation.output

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserOutput(
    @SerialName("id")
    val id: Long,
    @SerialName("email")
    val email: String,
)
