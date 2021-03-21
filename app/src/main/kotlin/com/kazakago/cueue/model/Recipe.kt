package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    @SerialName("id")
    val id: RecipeId,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("url")
    val url: String?,
    @SerialName("image")
    val image: ContentSerializer?,
    @SerialName("tags")
    val tags: List<Tag>,
)
