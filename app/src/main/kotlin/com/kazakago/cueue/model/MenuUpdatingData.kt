package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class MenuUpdatingData(
    @SerialName("memo")
    val memo: String? = null,
    @SerialName("date")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate? = null,
    @SerialName("time_frame")
    val timeFrame: TimeFrame? = null,
    @SerialName("recipe_ids")
    val recipeIds: List<RecipeId>? = null,
)
