package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.exception.EntityDuplicateException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

class ErrorController {

    suspend fun handle(call: ApplicationCall, exception: Exception) {
        when (exception) {
            is NoSuchElementException -> call.respond(HttpStatusCode.NotFound)
            is EntityDuplicateException -> call.respond(HttpStatusCode.Conflict)
            is MissingRequestParameterException -> call.respond(HttpStatusCode.BadRequest)
            else -> throw exception
        }
    }
}
