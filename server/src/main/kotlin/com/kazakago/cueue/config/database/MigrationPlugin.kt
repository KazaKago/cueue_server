package com.kazakago.cueue.config.database

import io.ktor.server.application.*
import org.flywaydb.core.Flyway

val Migration = createApplicationPlugin(name = "Migration") {
    val environment = environment ?: throw IllegalStateException()
    val connectionInfo = ConnectionInfo(environment.config.property("app.database.url").getString())
    val flyway = Flyway.configure().dataSource(
        connectionInfo.jdbcUrl,
        connectionInfo.user,
        connectionInfo.password,
    ).load()
    flyway.migrate()
}
