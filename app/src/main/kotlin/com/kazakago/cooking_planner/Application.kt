package com.kazakago.cooking_planner

import com.kazakago.cooking_planner.route.appRouting
import com.kazakago.cooking_planner.route.handleException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        handleException()
    }
    appRouting()
}
