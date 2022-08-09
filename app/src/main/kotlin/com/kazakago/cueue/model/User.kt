package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class User(
    @Transient
    @SerialName("id")
    val id: UserId = throw NullPointerException(),
    @SerialName("workspace")
    val workspace: Workspace?,
) {
    fun requireWorkspace() = workspace ?: throw NoSuchElementException()

    fun toRegistrationData(workspaceId: WorkspaceId? = workspace?.id): UserRegistrationData {
        return UserRegistrationData(
            workspaceId = workspaceId,
        )
    }
}
