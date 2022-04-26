package com.kazakago.cueue.controller

import com.kazakago.cueue.exception.ImageDecodeException
import com.kazakago.cueue.exception.UnauthorizedException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException

class ErrorController {

    suspend fun handle(call: ApplicationCall, exception: Exception) {
        call.application.environment.log.info(createLogMessage(exception))
        when (exception) {
            is NoSuchElementException -> call.respond(HttpStatusCode.NotFound)
            is EntityNotFoundException -> call.respond(HttpStatusCode.NotFound)
            is BadRequestException -> call.respond(HttpStatusCode.BadRequest)
            is UnauthorizedException -> call.respond(HttpStatusCode.Unauthorized)
            is ImageDecodeException -> call.respond(HttpStatusCode.BadRequest)
            else -> throw exception
        }
    }

    private fun createLogMessage(throwable: Throwable): String {
        val cause = throwable.cause
        return throwable.toString() + if (cause != null) "\ncause: ${createLogMessage(cause)}" else ""
    }
}
