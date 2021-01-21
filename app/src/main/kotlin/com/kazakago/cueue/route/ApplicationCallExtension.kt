package com.kazakago.cueue.route

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*

suspend inline fun <reified T : Any> ApplicationCall.receiveOrThrow(): T {
    try {
        return receive()
    } catch (exception: Exception) {
        throw ParameterConversionException("N/A", T::class.qualifiedName ?: "")
    }
}

fun <T> Parameters.getInt(name: String, transform: ((param: Int) -> T)): T {
    return transform(getInt(name))
}

fun Parameters.getInt(name: String): Int {
    return getIntOrNull(name) ?: throw MissingRequestParameterException(name)
}

fun <T> Parameters.getIntOrNull(name: String, transform: ((param: Int) -> T)): T? {
    return getIntOrNull(name)?.let { transform(it) }
}

fun Parameters.getIntOrNull(name: String): Int? {
    return try {
        get(name)?.toInt()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

fun <T> Parameters.getLong(name: String, transform: ((param: Long) -> T)): T {
    return transform(getLong(name))
}

fun Parameters.getLong(name: String): Long {
    return getLongOrNull(name) ?: throw MissingRequestParameterException(name)
}

fun <T> Parameters.getLongOrNull(name: String, transform: ((param: Long) -> T)): T? {
    return getLongOrNull(name)?.let { transform(it) }
}

fun Parameters.getLongOrNull(name: String): Long? {
    return try {
        get(name)?.toLong()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

fun <T> Parameters.getString(name: String, transform: ((param: String) -> T)): T {
    return transform(getString(name))
}

fun Parameters.getString(name: String): String {
    return getStringOrNull(name) ?: throw MissingRequestParameterException(name)
}

fun <T> Parameters.getStringOrNull(name: String, transform: ((param: String) -> T)): T? {
    return getStringOrNull(name)?.let { transform(it) }
}

fun Parameters.getStringOrNull(name: String): String? {
    return get(name)
}
