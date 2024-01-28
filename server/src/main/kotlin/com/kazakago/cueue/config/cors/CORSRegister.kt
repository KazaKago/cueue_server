package com.kazakago.cueue.config.cors

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.plugins.cors.CORSConfig

fun CORSConfig.register(application: ApplicationCallPipeline) {
    val environment = application.environment ?: throw IllegalStateException()
    val host = environment.config.property("app.url.host").getString()
    val schemes = environment.config.property("app.url.schemes").getString().filterNot { it.isWhitespace() }.split(",")
    val subDomains = environment.config.property("app.url.subDomains").getString().filterNot { it.isWhitespace() }.split(",")
    allowHost(host, schemes, subDomains)
    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.Authorization)
    allowHeader("Api-Version")
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Patch)
    allowMethod(HttpMethod.Delete)
    allowNonSimpleContentTypes = true
}
