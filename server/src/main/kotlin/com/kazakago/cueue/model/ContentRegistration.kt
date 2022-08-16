package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentRegistration(
    @SerialName("data")
    val data: String,
)
