package com.kazakago.cooking_planner.controller

import io.ktor.application.*
import io.ktor.response.*

class RootController {

    suspend fun index(call: ApplicationCall) {
        call.respond("Hello world from Ktor!!")
    }
}
