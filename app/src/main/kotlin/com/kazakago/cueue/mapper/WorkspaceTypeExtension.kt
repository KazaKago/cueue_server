package com.kazakago.cueue.mapper

import com.kazakago.cueue.model.WorkspaceType

fun WorkspaceType.rawValue(): String {
    return when (this) {
        WorkspaceType.Personal -> "personal"
    }
}
