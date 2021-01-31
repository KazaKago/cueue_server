package com.kazakago.cueue.route

import com.kazakago.cueue.controller.ErrorController
import io.ktor.application.*
import io.ktor.features.*

fun StatusPages.Configuration.handle() {
    exception<Exception> { cause ->
        val errorController = ErrorController()
        errorController.handle(call, cause)
    }
}
