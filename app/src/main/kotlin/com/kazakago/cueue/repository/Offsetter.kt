package com.kazakago.cueue.repository

import io.ktor.features.*
import org.jetbrains.exposed.dao.Entity

fun <ID, ENTITY : Entity<ID>> Iterable<ENTITY>.getOffset(afterId: ID?): Long {
    return if (afterId != null) {
        val index = indexOfFirst { it.id.value == afterId }
        if (index < 0) throw MissingRequestParameterException("after_id ($afterId)")
        index + 1L
    } else {
        0L
    }
}
