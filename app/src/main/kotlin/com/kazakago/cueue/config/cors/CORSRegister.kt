package com.kazakago.cueue.config.cors

import io.ktor.http.*
import io.ktor.server.plugins.cors.*

fun CORSConfig.register() {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.Authorization)
    allowHeader("Api-Version")
    allowHeader("AppCheck-Token")
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Patch)
    allowMethod(HttpMethod.Delete)
    allowCredentials = true
    allowNonSimpleContentTypes = true
}
