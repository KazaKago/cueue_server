package com.kazakago.cueue.config.sentry

import io.ktor.server.application.createApplicationPlugin

val Sentry = createApplicationPlugin(name = "Sentry") {
    val environment = environment ?: throw IllegalStateException()
    val sentryDsn = environment.config.property("app.sentry.dsn").getString()
    if (sentryDsn.isNotBlank()) {
        io.sentry.Sentry.init { options ->
            options.dsn = sentryDsn
        }
    }
}
