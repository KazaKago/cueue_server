package com.kazakago.cueue.config.database

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

val Database = createApplicationPlugin(name = "Database") {
    val environment = environment ?: throw IllegalStateException()
    Database.connect(
        url = environment.config.property("app.database.url").getString(),
        driver = "org.postgresql.Driver",
        user = environment.config.property("app.database.user").getString(),
        password = environment.config.property("app.database.password").getString(),
    )
}
