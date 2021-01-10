package com.kazakago.cooking_planner.presentation.route

import com.kazakago.cooking_planner.presentation.controller.ErrorController
import io.ktor.application.*
import io.ktor.features.*

fun StatusPages.Configuration.handleException() {
    exception<Exception> { cause ->
        val errorController = ErrorController()
        errorController.handle(call, cause)
    }
}
