package com.kazakago.cueue.controller

import com.kazakago.cueue.exception.ImageDecodeException
import com.kazakago.cueue.exception.UnauthorizedException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.sentry.Sentry
import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException

class ErrorController {

    suspend fun handle(call: ApplicationCall, exception: Exception) {
        when (exception) {
            is NoSuchElementException -> call.respond(HttpStatusCode.NotFound)
            is EntityNotFoundException -> call.respond(HttpStatusCode.NotFound)
            is BadRequestException -> call.respond(HttpStatusCode.BadRequest)
            is UnauthorizedException -> call.respond(HttpStatusCode.Unauthorized)
            is ImageDecodeException -> call.respond(HttpStatusCode.BadRequest)
            else -> {
                call.application.environment.log.info(createLogMessage(exception))
                Sentry.captureException(exception)
                throw exception
            }
        }
    }

    private fun createLogMessage(throwable: Throwable): String {
        val cause = throwable.cause
        return throwable.toString() + if (cause != null) "\ncause: ${createLogMessage(cause)}" else ""
    }
}
