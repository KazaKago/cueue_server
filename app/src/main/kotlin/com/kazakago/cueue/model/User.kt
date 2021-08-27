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
    fun validate(workspaceId: WorkspaceId) {
        val isContained = workspaces.map { it.id }.contains(workspaceId)
        if (!isContained) throw NotFoundException()
    }
}
