package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.database.entity.RecipeEntity
import com.kazakago.cooking_planner.model.RecipeId
import com.kazakago.cooking_planner.model.RecipeTags

class RecipeTagsMapper(private val tagMapper: TagMapper) {

    fun toModel(recipe: RecipeEntity): RecipeTags {
        return RecipeTags(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            description = recipe.description,
            tags = recipe.tags.map { tagMapper.toModel(it) }
        )
    }
}
