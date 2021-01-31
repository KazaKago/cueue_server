package com.kazakago.cueue

import com.kazakago.cueue.auth.firebase.AuthConfig.configure
import com.kazakago.cueue.auth.firebase.firebase
import com.kazakago.cueue.config.database.Database
import com.kazakago.cueue.config.database.Migration
import com.kazakago.cueue.config.firebase.Firebase
import com.kazakago.cueue.config.koin.register
import com.kazakago.cueue.route.appRouting
import com.kazakago.cueue.route.handle
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.koin.ktor.ext.Koin

fun Application.module() {
    install(Database)
    install(Migration)
    install(Firebase)
    install(DefaultHeaders)
    install(CallLogging)
    install(Koin) {
        register()
    }
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        handle()
    }
    install(Authentication) {
        firebase { configure() }
    }
    appRouting()
}
