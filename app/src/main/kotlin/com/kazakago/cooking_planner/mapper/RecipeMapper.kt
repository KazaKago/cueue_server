package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.database.entity.RecipeEntity
import com.kazakago.cooking_planner.model.RecipeId
import com.kazakago.cooking_planner.model.Recipe

class RecipeMapper {

    fun toModel(recipe: RecipeEntity): Recipe {
        return Recipe(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            description = recipe.description,
        )
    }
}
