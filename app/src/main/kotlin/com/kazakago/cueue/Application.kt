package com.kazakago.cueue

import com.kazakago.cueue.route.appRouting
import com.kazakago.cueue.route.handleException
import com.kazakago.cueue.route.registerEnv
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
