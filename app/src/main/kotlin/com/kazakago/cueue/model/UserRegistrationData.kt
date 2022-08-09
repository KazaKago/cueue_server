package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationData(
    @SerialName("workspace_id")
    val workspaceId: WorkspaceId?,
)
