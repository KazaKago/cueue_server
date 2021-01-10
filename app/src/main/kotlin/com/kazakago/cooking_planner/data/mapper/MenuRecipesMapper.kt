package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.data.database.entity.MenuEntity
import com.kazakago.cooking_planner.domain.model.MenuId
import com.kazakago.cooking_planner.domain.model.MenuRecipes

class MenuRecipesMapper(private val timeFrameMapper: TimeFrameMapper, private val recipeMapper: RecipeMapper) {

    fun toModel(menu: MenuEntity): MenuRecipes {
        return MenuRecipes(
            id = MenuId(menu.id.value),
            memo = menu.memo,
            date = menu.date,
            timeFrame = timeFrameMapper.toModel(menu.timeFrame),
            recipes = menu.recipes.map { recipeMapper.toModel(it) }
        )
    }
}
