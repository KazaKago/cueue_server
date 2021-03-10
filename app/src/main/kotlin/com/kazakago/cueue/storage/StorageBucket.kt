package com.kazakago.cueue.storage

import java.net.URL

object StorageBucket {
    private const val host = "https://storage.googleapis.com"
    lateinit var bucketName: String

    fun createPublicUrl(path: String): URL {
        return URL("$host/$bucketName/$path")
    }
}
