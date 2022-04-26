package com.kazakago.cueue.controller

import io.ktor.server.application.*
import io.ktor.server.response.*

class RootController {

    suspend fun index(call: ApplicationCall) {
        call.respond("Hello world from Ktor!!")
    }
}
