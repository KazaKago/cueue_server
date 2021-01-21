package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationData(
    @SerialName("email")
    val email: Email,
    @SerialName("nickname")
    val nickname: String,
)
