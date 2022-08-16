package com.kazakago.cueue.mapper

import com.kazakago.cueue.model.TimeFrame

class TimeFrameMapper {

    fun toModel(timeFrame: String): TimeFrame {
        return TimeFrame.values().first { it.rawValue() == timeFrame }
    }
}
