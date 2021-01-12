package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.model.TimeFrame

fun TimeFrame.rawValue(): String {
    return when (this) {
        TimeFrame.Breakfast -> "breakfast"
        TimeFrame.Lunch -> "lunch"
        TimeFrame.SnackTime -> "snack_time"
        TimeFrame.Dinner -> "dinner"
    }
}
