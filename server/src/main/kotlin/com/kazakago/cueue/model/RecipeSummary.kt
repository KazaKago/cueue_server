package com.kazakago.cueue.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeSummary(
    @SerialName("id")
    val id: RecipeId,
    @SerialName("title")
    val title: String,
    @SerialName("image")
    val image: ContentSerializer?,
    @SerialName("last_cooking_at")
    @Serializable(with = LocalDateSerializer::class)
    val lastCookingAt: LocalDate?,
)
