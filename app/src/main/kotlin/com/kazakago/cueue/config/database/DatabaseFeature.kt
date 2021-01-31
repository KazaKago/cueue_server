package com.kazakago.cueue.config.database

import com.kazakago.cueue.database.setting.DbSettings
import io.ktor.application.*
import io.ktor.util.*

class Database {

    companion object Feature : ApplicationFeature<Application, Unit, Unit> {
        override val key: AttributeKey<Unit> = AttributeKey("Database")

        override fun install(pipeline: Application, configure: Unit.() -> Unit) {
            DbSettings.initialize(
                url = pipeline.environment.config.property("app.database.url").getString(),
                user = pipeline.environment.config.property("app.database.user").getString(),
                password = pipeline.environment.config.property("app.database.password").getString(),
            )
        }
    }
}
