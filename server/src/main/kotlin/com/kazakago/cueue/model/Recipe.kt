package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

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
    @SerialName("images")
    val images: List<ContentSerializer>,
    @SerialName("tags")
    val tags: List<Tag>,
    @SerialName("cooking_histories")
    val cookingHistories: List<String>,
    @SerialName("cooking_count")
    val cookingCount: Long,
    @SerialName("created_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @SerialName("updated_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
)
