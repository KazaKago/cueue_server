package com.kazakago.cueue.mapper

import com.kazakago.cueue.model.TimeFrame

fun TimeFrame.rawValue(): String {
    return when (this) {
        TimeFrame.Breakfast -> "breakfast"
        TimeFrame.Lunch -> "lunch"
        TimeFrame.SnackTime -> "snack_time"
        TimeFrame.Dinner -> "dinner"
    }
}
