package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.database.entity.TagEntity
import com.kazakago.cooking_planner.model.TagName
import com.kazakago.cooking_planner.model.TagRecipes

class TagRecipesMapper(private val recipeMapper: RecipeMapper) {

    fun toModel(tag: TagEntity): TagRecipes {
        return TagRecipes(
            name = TagName(tag.name),
            recipes = tag.recipes.map { recipeMapper.toModel(it) }
        )
    }
}
