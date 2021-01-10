package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.domain.model.TimeFrame

class TimeFrameMapper {

    fun toModel(timeFrame: String): TimeFrame {
        return TimeFrame.values().first { it.rawValue() == timeFrame }
    }
}
