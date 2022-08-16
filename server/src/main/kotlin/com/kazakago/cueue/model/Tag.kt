package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    @SerialName("id")
    val id: TagId,
    @SerialName("name")
    val name: String,
)
