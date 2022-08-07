package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Workspace(
    @Transient
    @SerialName("id")
    val id: WorkspaceId = throw NullPointerException(),
    @SerialName("name")
    val name: String,
    @SerialName("users")
    val users: List<UserSummary>,
)
