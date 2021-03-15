package com.kazakago.cueue.model

import com.kazakago.cueue.mapper.rawValue

enum class WorkspaceType {
    Personal;

    companion object {
        fun resolve(value: String): WorkspaceType {
            return values().first { it.rawValue() == value }
        }
    }
}
