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

inline fun <reified T : Principal> ApplicationCall.requirePrincipal(): T {
    return try {
        principal()!!
    } catch (exception: Exception) {
        throw UnauthorizedException(exception)
    }
}

inline fun <T> Parameters.requireInt(name: String, transform: ((param: Int) -> T)): T {
    return getInt(name, transform) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getInt(name: String, transform: ((param: Int) -> T)): T? {
    return getInt(name)?.let { transform(it) }
}

fun Parameters.requireInt(name: String): Int {
    return getInt(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getInt(name: String): Int? {
    return try {
        get(name)?.toInt()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

inline fun <T> Parameters.requireLong(name: String, transform: ((param: Long) -> T)): T {
    return getLong(name, transform) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getLong(name: String, transform: ((param: Long) -> T)): T? {
    return getLong(name)?.let { transform(it) }
}

fun Parameters.requireLong(name: String): Long {
    return getLong(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getLong(name: String): Long? {
    return try {
        get(name)?.toLong()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

inline fun <T> Parameters.requireString(name: String, transform: ((param: String) -> T)): T {
    return getString(name, transform) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getString(name: String, transform: ((param: String) -> T)): T? {
    return getString(name)?.let { transform(it) }
}

fun Parameters.requireString(name: String): String {
    return getString(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getString(name: String): String? {
    return get(name)
}

inline fun <T> Parameters.requireIntAll(name: String, transform: ((param: Int) -> T)): List<T> {
    return getIntAll(name, transform) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getIntAll(name: String, transform: ((param: Int) -> T)): List<T>? {
    return getIntAll(name)?.map { transform(it) }
}

fun Parameters.requireIntAll(name: String): List<Int> {
    return getIntAll(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getIntAll(name: String): List<Int>? {
    return try {
        getAll(name)?.map { it.toInt() }
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

inline fun <T> Parameters.requireLongAll(name: String, transform: ((param: Long) -> T)): List<T> {
    return getLongAll(name, transform) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getLongAll(name: String, transform: ((param: Long) -> T)): List<T>? {
    return getLongAll(name)?.map { transform(it) }
}

fun Parameters.requireLongAll(name: String): List<Long> {
    return getLongAll(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getLongAll(name: String): List<Long>? {
    return try {
        getAll(name)?.map { it.toLong() }
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
}

inline fun <T> Parameters.requireStringAll(name: String, transform: ((param: String) -> T)): List<T> {
    return getStringAll(name, transform) ?: throw MissingRequestParameterException(name)
}

inline fun <T> Parameters.getStringAll(name: String, transform: ((param: String) -> T)): List<T>? {
    return getStringAll(name)?.map { transform(it) }
}

fun Parameters.requireStringAll(name: String): List<String> {
    return getStringAll(name) ?: throw MissingRequestParameterException(name)
}

fun Parameters.getStringAll(name: String): List<String>? {
    return getAll(name)
}
