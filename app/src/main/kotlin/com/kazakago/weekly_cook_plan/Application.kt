package com.kazakago.weekly_cook_plan

import com.kazakago.weekly_cook_plan.route.appRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }
    appRouting()
}
