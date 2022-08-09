package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationData(
    @SerialName("display_name")
    val displayName: String,
    @SerialName("photo_key")
    val photoKey: String?,
)
