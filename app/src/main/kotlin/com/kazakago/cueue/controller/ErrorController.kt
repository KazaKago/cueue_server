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
        call.application.environment.log.info(createLogMessage(exception))
        when (exception) {
            is NoSuchElementException -> call.respond(HttpStatusCode.NotFound)
            is EntityNotFoundException -> call.respond(HttpStatusCode.NotFound)
            is EntityDuplicateException -> call.respond(HttpStatusCode.Conflict)
            is BadRequestException -> call.respond(HttpStatusCode.BadRequest)
            is UnauthorizedException -> call.respond(HttpStatusCode.Unauthorized)
            else -> throw exception
        }
    }

    private fun createLogMessage(throwable: Throwable): String {
        return throwable.toString() + if (throwable.cause != null) "\ncause: ${createLogMessage(throwable.cause!!)}" else ""
    }
}
