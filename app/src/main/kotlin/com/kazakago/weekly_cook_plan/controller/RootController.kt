package com.kazakago.weekly_cook_plan.controller

import io.ktor.application.*
import io.ktor.response.*

class RootController {

    suspend fun index(call: ApplicationCall) {
        call.respond("Hello world from Ktor!!")
    }
}
