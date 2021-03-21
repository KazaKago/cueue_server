package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeRegistrationData(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("image_keys")
    val imageKeys: List<String>? = null,
    @SerialName("tag_ids")
    val tagIds: List<TagId>? = null,
)
