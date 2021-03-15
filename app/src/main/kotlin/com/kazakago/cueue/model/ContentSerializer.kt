package com.kazakago.cueue.model

import com.kazakago.cueue.storage.StorageBucket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentSerializer(
    @SerialName("key")
    val key: String,
) {
    @SerialName("url")
    val url: String = StorageBucket.createPublicUrl(key).toString()
}
