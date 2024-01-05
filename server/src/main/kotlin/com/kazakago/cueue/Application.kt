package com.kazakago.cueue

import com.kazakago.cueue.auth.firebase.AuthConfig.configure
import com.kazakago.cueue.auth.firebase.firebase
import com.kazakago.cueue.config.cors.register
import com.kazakago.cueue.config.database.Database
import com.kazakago.cueue.config.database.Migration
import com.kazakago.cueue.config.firebase.Firebase
import com.kazakago.cueue.config.koin.Koin
import com.kazakago.cueue.config.koin.register
import com.kazakago.cueue.config.sentry.Sentry
import com.kazakago.cueue.config.statuspage.handle
import com.kazakago.cueue.route.appRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import io.ktor.server.plugins.statuspages.StatusPages

fun Application.module() {
    install(Database)
    install(Migration)
    install(Firebase)
    install(DefaultHeaders)
    install(XForwardedHeaders)
    install(CallLogging)
    install(Sentry)
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
