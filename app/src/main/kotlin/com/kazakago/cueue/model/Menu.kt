package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Menu(
    @SerialName("id")
    val id: MenuId,
    @SerialName("memo")
    val memo: String,
    @SerialName("date")
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    @SerialName("time_frame")
    val timeFrame: TimeFrame,
    @SerialName("recipes")
    val recipes: List<RecipeSummary>,
)
