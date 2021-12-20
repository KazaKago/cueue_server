package com.kazakago.cueue.model

import io.ktor.features.*

data class UnsafeWorkspaceId(val value: Long) {
    fun validate(user: User): WorkspaceId {
        val isContained = user.workspaces.map { it.id.value }.contains(value)
        if (isContained) {
            return WorkspaceId(value)
        } else {
            throw NotFoundException()
        }
    }
}

