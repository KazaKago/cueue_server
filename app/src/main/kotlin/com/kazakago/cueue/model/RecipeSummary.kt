package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

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
