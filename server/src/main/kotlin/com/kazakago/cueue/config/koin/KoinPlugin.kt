package com.kazakago.cueue.config.koin

import io.ktor.server.application.createApplicationPlugin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

val Koin = createApplicationPlugin(name = "Koin", KoinApplication::init) {
    startKoin(pluginConfig)
}
