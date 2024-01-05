package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.MenuEntity
import com.kazakago.cueue.model.Menu
import com.kazakago.cueue.model.MenuId

class MenuMapper(private val timeFrameMapper: TimeFrameMapper, private val recipeSummaryMapper: RecipeSummaryMapper) {

    fun toModel(menu: MenuEntity): Menu {
        return Menu(
            id = MenuId(menu.id.value),
            memo = menu.memo,
            date = menu.date,
            timeFrame = timeFrameMapper.toModel(menu.timeFrame),
            recipes = menu.recipes
                .sortedByDescending { it.id }
                .map { recipeSummaryMapper.toModel(it) }
        )
    }
}
