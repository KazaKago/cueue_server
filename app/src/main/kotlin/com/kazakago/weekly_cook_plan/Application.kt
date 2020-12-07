package com.kazakago.weekly_cook_plan

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.module() {
    install(Routing) {
        get("/") {
            call.respond("Hello world from Ktor!!")
        }
    }
}
