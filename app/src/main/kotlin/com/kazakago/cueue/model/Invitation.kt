package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Invitation(
    @SerialName("code")
    val code: InvitationCode,
    @SerialName("workspace")
    val workspace: Workspace,
)
