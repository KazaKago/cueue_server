package com.kazakago.cueue.config.database

import io.ktor.application.*
import io.ktor.util.*
import org.flywaydb.core.Flyway

class Migration {

    companion object Feature : ApplicationFeature<Application, Unit, Unit> {
        override val key: AttributeKey<Unit> = AttributeKey("Migration")

        override fun install(pipeline: Application, configure: Unit.() -> Unit) {
            val flyway = Flyway.configure().dataSource(
                pipeline.environment.config.property("app.database.url").getString(),
                pipeline.environment.config.property("app.database.user").getString(),
                pipeline.environment.config.property("app.database.password").getString(),
            ).load()
            flyway.migrate()
        }
    }
}
