package com.kazakago.cueue.storage

import java.net.URL

object StorageBucketUrl {
    private const val value = "https://storage.googleapis.com/cueue-staging.appspot.com/"

    fun createUrl(path: String): URL {
        return URL(value + path)
    }
}
