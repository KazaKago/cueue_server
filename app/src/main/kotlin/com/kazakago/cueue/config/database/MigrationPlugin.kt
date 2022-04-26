package com.kazakago.cueue.config.database

import io.ktor.server.application.*
import org.flywaydb.core.Flyway

val Migration = createApplicationPlugin(name = "Migration") {
    val environment = environment ?: throw IllegalStateException()
    val flyway = Flyway.configure().dataSource(
        environment.config.property("app.database.url").getString(),
        environment.config.property("app.database.user").getString(),
        environment.config.property("app.database.password").getString(),
    ).load()
    flyway.migrate()
}
