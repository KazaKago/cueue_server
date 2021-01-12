package com.kazakago.cooking_planner.route

import com.kazakago.cooking_planner.controller.ErrorController
import io.ktor.application.*
import io.ktor.features.*

fun StatusPages.Configuration.handleException() {
    exception<Exception> { cause ->
        val errorController = ErrorController()
        errorController.handle(call, cause)
    }
}
