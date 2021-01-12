package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.database.entity.MenuEntity
import com.kazakago.cooking_planner.model.MenuId
import com.kazakago.cooking_planner.model.Menu

class MenuMapper(private val timeFrameMapper: TimeFrameMapper) {

    fun toModel(menu: MenuEntity): Menu {
        return Menu(
            id = MenuId(menu.id.value),
            memo = menu.memo,
            date = menu.date,
            timeFrame = timeFrameMapper.toModel(menu.timeFrame),
        )
    }
}
