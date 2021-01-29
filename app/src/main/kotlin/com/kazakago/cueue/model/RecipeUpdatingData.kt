package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeUpdatingData(
    @SerialName("title")
    val title: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("tag_names")
    val tagNames: List<TagName>? = null,
)
