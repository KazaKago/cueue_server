package com.kazakago.cooking_planner.route

import io.ktor.features.*
import io.ktor.http.*
import io.ktor.util.*

inline fun Parameters.getAsInt(name: String, block: (parameter: Int) -> Unit) {
    val parameter = try {
        getOrFail(name).toInt()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
    block(parameter)
}

inline fun Parameters.getAsLong(name: String, block: (parameter: Long) -> Unit) {
    val parameter = try {
        getOrFail(name).toLong()
    } catch (exception: NumberFormatException) {
        throw MissingRequestParameterException(name)
    }
    block(parameter)
}

inline fun Parameters.getAsString(name: String, block: (parameter: String) -> Unit) {
    val parameter = getOrFail(name)
    block(parameter)
}
