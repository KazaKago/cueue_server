package com.kazakago.cueue.model

import io.ktor.server.plugins.*

data class UnsafeWorkspaceId(val value: Long) {

    fun validate(user: User): WorkspaceId {
        if (user.workspace?.id?.value == value) {
            return WorkspaceId(value)
        } else {
            throw NotFoundException()
        }
    }
}

