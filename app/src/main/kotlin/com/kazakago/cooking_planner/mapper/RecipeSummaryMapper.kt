package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.database.entity.RecipeEntity
import com.kazakago.cooking_planner.model.RecipeId
import com.kazakago.cooking_planner.model.RecipeSummary

class RecipeSummaryMapper {

    fun toModel(recipe: RecipeEntity): RecipeSummary {
        return RecipeSummary(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
        )
    }
}
