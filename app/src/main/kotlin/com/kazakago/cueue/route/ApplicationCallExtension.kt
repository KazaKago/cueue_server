package com.kazakago.cueue.route

import com.kazakago.cueue.exception.UnauthorizedException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*

suspend inline fun <reified T : Any> ApplicationCall.requireReceive(): T {
    try {
        return receive()
    } catch (exception: Exception) {
        throw ParameterConversionException("N/A", T::class.qualifiedName ?: "", exception)
    }
}

inline fun <T> Parameters.requireInt(name: String, transform: ((param: Int) -> T)): T {
    return transform(requireInt(name))
}

fun Parameters.requireInt(name: String): Int {
    return getInt(name) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getInt(name: String, transform: ((param: Int) -> T)): T? {
    return getInt(name)?.let { transform(it) }
}

fun Parameters.getInt(name: String): Int? {
    return try {
        get(name)?.toInt()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

inline fun <T> Parameters.requireLong(name: String, transform: ((param: Long) -> T)): T {
    return transform(requireLong(name))
}

fun Parameters.requireLong(name: String): Long {
    return getLong(name) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getLong(name: String, transform: ((param: Long) -> T)): T? {
    return getLong(name)?.let { transform(it) }
}

fun Parameters.getLong(name: String): Long? {
    return try {
        get(name)?.toLong()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

inline fun <T> Parameters.requireString(name: String, transform: ((param: String) -> T)): T {
    return transform(requireString(name))
}

fun Parameters.requireString(name: String): String {
    return getString(name) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getString(name: String, transform: ((param: String) -> T)): T? {
    return getString(name)?.let { transform(it) }
}

fun Parameters.getString(name: String): String? {
    return get(name)
}

inline fun <reified T : Principal> ApplicationCall.requirePrincipal(): T {
    return try {
        principal()!!
    } catch (exception: Exception) {
        throw UnauthorizedException(exception)
    }
}
