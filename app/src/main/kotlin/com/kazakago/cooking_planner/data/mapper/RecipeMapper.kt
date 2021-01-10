package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.data.database.entity.RecipeEntity
import com.kazakago.cooking_planner.domain.model.Recipe
import com.kazakago.cooking_planner.domain.model.RecipeId
import com.kazakago.cooking_planner.domain.model.RecipeImpl

class RecipeMapper {

    fun toModel(recipe: RecipeEntity): Recipe {
        return RecipeImpl(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            description = recipe.description,
        )
    }
}
