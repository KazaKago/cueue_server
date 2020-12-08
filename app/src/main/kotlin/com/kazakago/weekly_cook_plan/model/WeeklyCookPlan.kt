package com.kazakago.weekly_cook_plan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyCookPlan(
    @SerialName("sunday")
    val sunday: CookPlan,
    @SerialName("monday")
    val monday: CookPlan,
    @SerialName("tuesday")
    val tuesday: CookPlan,
    @SerialName("wednesday")
    val wednesday: CookPlan,
    @SerialName("thursday")
    val thursday: CookPlan,
    @SerialName("friday")
    val friday: CookPlan,
    @SerialName("saturday")
    val saturday: CookPlan,
)
