package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagRegistrationData(
    @SerialName("name")
    val name: String,
)
