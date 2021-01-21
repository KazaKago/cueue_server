package com.kazakago.cueue.route

import com.kazakago.cueue.database.setting.DbSettings
import io.ktor.application.*

fun Application.registerEnv() {
    DbSettings.initialize(
        url = environment.config.property("app.database.url").getString(),
        user = environment.config.property("app.database.user").getString(),
        password = environment.config.property("app.database.password").getString(),
    )
}
