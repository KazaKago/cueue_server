package com.kazakago.cueue

import com.kazakago.cueue.auth.firebase.firebase
import com.kazakago.cueue.config.database.initializeDatabase
import com.kazakago.cueue.config.firebase.AuthConfig.configure
import com.kazakago.cueue.config.firebase.initializeFirebase
import com.kazakago.cueue.config.koin.register
import com.kazakago.cueue.route.appRouting
import com.kazakago.cueue.route.handleException
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.koin.ktor.ext.Koin

fun Application.module() {
    initializeDatabase()
    initializeFirebase()
    install(DefaultHeaders)
    install(CallLogging)
    install(Koin) {
        register()
    }
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        handleException()
    }
    install(Authentication) {
        firebase { configure() }
    }
    appRouting()
}
