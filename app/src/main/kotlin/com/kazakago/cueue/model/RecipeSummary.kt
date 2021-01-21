package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeSummary(
    @SerialName("id")
    val id: RecipeId,
    @SerialName("title")
    val title: String,
)
