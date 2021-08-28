package com.kazakago.cueue.model

import io.ktor.features.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: UserId,
    @SerialName("workspaces")
    val workspaces: List<Workspace>,
) {
    fun validate(unsafeWorkspaceId: UnsafeWorkspaceId): WorkspaceId {
        val isContained = workspaces.map { it.id.value }.contains(unsafeWorkspaceId.value)
        if (isContained) {
            return WorkspaceId(unsafeWorkspaceId.value)
        } else {
            throw NotFoundException()
        }
    }
}
