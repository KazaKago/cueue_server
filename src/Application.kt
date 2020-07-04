package com.kazakago

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Application.module() {
    install(Routing) {
        get("/") {
            call.respond("Hello world from Ktor!!!!")
        }
    }
}
