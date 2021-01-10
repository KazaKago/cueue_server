package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.data.database.entity.TagEntity
import com.kazakago.cooking_planner.domain.model.TagName
import com.kazakago.cooking_planner.domain.model.TagRecipes

class TagRecipesMapper(private val recipeMapper: RecipeMapper) {

    fun toModel(tag: TagEntity): TagRecipes {
        return TagRecipes(
            name = TagName(tag.name),
            recipes = tag.recipes.map { recipeMapper.toModel(it) }
        )
    }
}
