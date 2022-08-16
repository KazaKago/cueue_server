package com.kazakago.cueue.config.cors

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.*

fun CORSConfig.register(application: ApplicationCallPipeline) {
    val environment = application.environment ?: throw IllegalStateException()
    val host = environment.config.property("app.url.host").getString()
    val schemes = environment.config.property("app.url.schemes").getList()
    val subDomains = environment.config.property("app.url.subDomains").getList()
    allowHost(host, schemes, subDomains)
    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.Authorization)
    allowHeader("Api-Version")
    allowHeader("AppCheck-Token")
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Patch)
    allowMethod(HttpMethod.Delete)
    allowNonSimpleContentTypes = true
}
