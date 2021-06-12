package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.MenuEntity
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuSummary

class MenuSummaryMapper(private val timeFrameMapper: TimeFrameMapper, private val recipeSummaryMapper: RecipeSummaryMapper) {

    fun toModel(menu: MenuEntity): MenuSummary {
        return MenuSummary(
            id = MenuId(menu.id.value),
            memo = menu.memo,
            date = menu.date,
            timeFrame = timeFrameMapper.toModel(menu.timeFrame),
            recipes = menu.recipes.map { recipeSummaryMapper.toModel(it) }
        )
    }
}
