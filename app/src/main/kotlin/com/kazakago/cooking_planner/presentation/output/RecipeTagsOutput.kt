package com.kazakago.cooking_planner.presentation.output

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeTagsOutput(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("tag_names")
    val tagNames: List<String>,
)
