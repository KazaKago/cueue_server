package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserSummary(
    @SerialName("id")
    val id: UserId,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("photo")
    val photo: ContentSerializer?,
)
