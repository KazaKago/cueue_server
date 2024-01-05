package com.kazakago.cueue.config.statuspage

import com.kazakago.cueue.controller.ErrorController
import io.ktor.server.plugins.statuspages.StatusPagesConfig

fun StatusPagesConfig.handle() {
    exception<Exception> { call, cause ->
        val errorController = ErrorController()
        errorController.handle(call, cause)
    }
}
