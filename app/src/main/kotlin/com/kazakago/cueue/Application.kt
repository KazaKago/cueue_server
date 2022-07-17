package com.kazakago.cueue

import com.kazakago.cueue.auth.firebase.AuthConfig.configure
import com.kazakago.cueue.auth.firebase.firebase
import com.kazakago.cueue.config.cors.register
import com.kazakago.cueue.config.database.Database
import com.kazakago.cueue.config.database.Migration
import com.kazakago.cueue.config.firebase.Firebase
import com.kazakago.cueue.config.koin.Koin
import com.kazakago.cueue.config.koin.register
import com.kazakago.cueue.route.appRouting
import com.kazakago.cueue.route.handle
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.statuspages.*

fun Application.module() {
    install(Database)
    install(Migration)
    install(Firebase)
    install(DefaultHeaders)
    install(XForwardedHeaders)
    install(CallLogging)
    install(CORS) {
        register(this@module)
    }
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
