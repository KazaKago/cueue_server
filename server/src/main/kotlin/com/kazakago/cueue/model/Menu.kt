package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Menu(
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
