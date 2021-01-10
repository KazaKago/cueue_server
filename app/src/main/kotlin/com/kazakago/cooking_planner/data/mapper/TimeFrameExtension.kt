package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.domain.model.TimeFrame

fun TimeFrame.rawValue(): String {
    return when (this) {
        TimeFrame.Breakfast -> "breakfast"
        TimeFrame.Lunch -> "lunch"
        TimeFrame.Dinner -> "dinner"
        TimeFrame.SnackTime -> "snack_time"
    }
}
