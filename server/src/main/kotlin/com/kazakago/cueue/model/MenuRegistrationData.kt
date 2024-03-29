package com.kazakago.cueue.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuRegistrationData(
    @SerialName("memo")
    val memo: String? = null,
    @SerialName("date")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    @SerialName("time_frame")
    val timeFrame: TimeFrame,
    @SerialName("recipe_ids")
    val recipeIds: List<RecipeId>? = null,
)
