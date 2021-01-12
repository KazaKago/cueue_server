package com.kazakago.cooking_planner.route

import io.ktor.features.*
import io.ktor.http.*

fun Parameters.getInt(name: String): Int {
    return getIntOrNull(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getIntOrNull(name: String): Int? {
    return try {
        get(name)?.toInt()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

fun Parameters.getLong(name: String): Long {
    return getLongOrNull(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getLongOrNull(name: String): Long? {
    return try {
        get(name)?.toLong()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

fun Parameters.getString(name: String): String {
    return getStringOrNull(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getStringOrNull(name: String): String? {
    return get(name)
}