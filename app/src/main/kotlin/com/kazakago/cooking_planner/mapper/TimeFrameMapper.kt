package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.model.TimeFrame

class TimeFrameMapper {

    fun toModel(timeFrame: String): TimeFrame {
        return TimeFrame.values().first { it.rawValue() == timeFrame }
    }
}
