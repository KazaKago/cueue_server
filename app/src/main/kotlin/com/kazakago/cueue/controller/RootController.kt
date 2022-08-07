package com.kazakago.cueue.controller

import io.ktor.server.application.*
import io.ktor.server.response.*

class RootController {

    suspend fun index(call: ApplicationCall) {
        call.respond("This is Cueue!")
    }
}
