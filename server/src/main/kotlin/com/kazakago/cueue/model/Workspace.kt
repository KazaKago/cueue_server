package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Workspace(
    @SerialName("id")
    val id: WorkspaceId,
    @SerialName("name")
    val name: String,
    @SerialName("users")
    val users: List<UserSummary>,
)
