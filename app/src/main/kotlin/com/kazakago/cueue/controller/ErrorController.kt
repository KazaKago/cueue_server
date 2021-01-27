package com.kazakago.cueue.controller

import com.kazakago.cueue.exception.EntityDuplicateException
import com.kazakago.cueue.exception.UnauthorizedException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException

class ErrorController {

    suspend fun handle(call: ApplicationCall, exception: Exception) {
        when (exception) {
            is NoSuchElementException -> call.respond(HttpStatusCode.NotFound)
            is EntityNotFoundException -> call.respond(HttpStatusCode.NotFound)
            is EntityDuplicateException -> call.respond(HttpStatusCode.Conflict)
            is MissingRequestParameterException -> call.respond(HttpStatusCode.BadRequest)
            is ParameterConversionException -> call.respond(HttpStatusCode.BadRequest)
            is UnauthorizedException -> call.respond(HttpStatusCode.Unauthorized)
            else -> throw exception
        }
    }
}
