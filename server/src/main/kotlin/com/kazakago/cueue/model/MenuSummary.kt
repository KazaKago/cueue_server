package com.kazakago.cueue.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuSummary(
    @SerialName("id")
    val id: MenuId,
    @SerialName("memo")
    val memo: String,
    @SerialName("date")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    @SerialName("time_frame")
    val timeFrame: TimeFrame,
    @SerialName("recipes")
    val recipes: List<RecipeSummary>,
)
