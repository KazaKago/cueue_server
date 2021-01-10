package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.data.database.entity.MenuEntity
import com.kazakago.cooking_planner.domain.model.Menu
import com.kazakago.cooking_planner.domain.model.MenuId
import com.kazakago.cooking_planner.domain.model.MenuImpl

class MenuMapper(private val timeFrameMapper: TimeFrameMapper) {

    fun toModel(menu: MenuEntity): Menu {
        return MenuImpl(
            id = MenuId(menu.id.value),
            memo = menu.memo,
            date = menu.date,
            timeFrame = timeFrameMapper.toModel(menu.timeFrame),
        )
    }
}
