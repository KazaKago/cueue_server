package com.kazakago.cueue.config.database

import java.net.URI

data class ConnectionInfo(private val urlStr: String) {

    val jdbcUrl: String
    val user: String
    val password: String

    init {
        val url = URI(urlStr)
        jdbcUrl = "jdbc:postgresql://${url.host}:${url.port}${url.path}"
        user = url.userInfo.split(":")[0]
        password = url.userInfo.split(":")[1]
    }
}
