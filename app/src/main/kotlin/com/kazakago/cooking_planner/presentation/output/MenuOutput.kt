package com.kazakago.cooking_planner.presentation.output

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuOutput(
    @SerialName("id")
    val id: Long,
    @SerialName("memo")
    val memo: String,
    @SerialName("date")
    val date: String,
    @SerialName("time_frame")
    val timeFrame: String,
)
