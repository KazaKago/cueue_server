package com.kazakago.cueue.controller

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class RootController {

    suspend fun index(call: ApplicationCall) {
        call.respond("This is Cueue!")
    }
}
