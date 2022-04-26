package com.kazakago.cueue.route

import com.kazakago.cueue.controller.ErrorController
import io.ktor.server.plugins.statuspages.*

fun StatusPagesConfig.handle() {
    exception<Exception> { call, cause ->
        val errorController = ErrorController()
        errorController.handle(call, cause)
    }
}
