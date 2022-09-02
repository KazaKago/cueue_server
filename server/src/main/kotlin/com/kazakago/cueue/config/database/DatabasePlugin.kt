package com.kazakago.cueue.config.database

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import java.net.URI

val Database = createApplicationPlugin(name = "Database") {
    val environment = environment ?: throw IllegalStateException()
    val connectionInfo = ConnectionInfo(environment.config.property("app.database.url").getString())
    Database.connect(
        url = connectionInfo.jdbcUrl,
        driver = "org.postgresql.Driver",
        user = connectionInfo.user,
        password = connectionInfo.password,
    )
}

private fun parseDatabaseUrl(url: URI): Triple<String, String, String> {
    val jdbcUrl = "jdbc:postgresql://${url.host}:${url.port}${url.path}"
    val user = url.userInfo.split(":")[0]
    val password = url.userInfo.split(":")[1]
    return Triple(jdbcUrl, user, password)
}
