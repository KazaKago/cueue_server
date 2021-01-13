package com.kazakago.cooking_planner

import com.kazakago.cooking_planner.route.appRouting
import com.kazakago.cooking_planner.route.handleException
import com.kazakago.cooking_planner.route.registerEnv
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*

fun Application.module() {
    registerEnv()
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
