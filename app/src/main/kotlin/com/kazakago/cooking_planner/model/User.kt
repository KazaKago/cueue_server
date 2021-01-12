package com.kazakago.cooking_planner.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: UserId,
    @SerialName("email")
    val email: Email,
)