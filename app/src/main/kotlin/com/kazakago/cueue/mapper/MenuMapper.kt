package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.MenuEntity
import com.kazakago.cueue.model.Menu
import com.kazakago.cueue.model.MenuId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MenuMapper(private val timeFrameMapper: TimeFrameMapper, private val recipeSummaryMapper: RecipeSummaryMapper) {

    suspend fun toModel(menu: MenuEntity): Menu {
        return newSuspendedTransaction {
            Menu(
                id = MenuId(menu.id.value),
                memo = menu.memo,
                date = menu.date,
                timeFrame = timeFrameMapper.toModel(menu.timeFrame),
                recipes = menu.recipes.map { recipeSummaryMapper.toModel(it) }
            )
        }
    }
}
