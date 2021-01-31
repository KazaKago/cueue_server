package com.kazakago.cueue.config.database

import io.ktor.application.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.Database

class Database {

    companion object Feature : ApplicationFeature<Application, Unit, Unit> {
        override val key: AttributeKey<Unit> = AttributeKey("Database")

        override fun install(pipeline: Application, configure: Unit.() -> Unit) {
            Database.connect(
                url = pipeline.environment.config.property("app.database.url").getString(),
                driver = "org.postgresql.Driver",
                user = pipeline.environment.config.property("app.database.user").getString(),
                password = pipeline.environment.config.property("app.database.password").getString(),
            )
        }
    }
}
