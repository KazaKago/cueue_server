package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.data.database.entity.RecipeEntity
import com.kazakago.cooking_planner.domain.model.RecipeId
import com.kazakago.cooking_planner.domain.model.RecipeTags
import com.kazakago.cooking_planner.domain.model.TagName

class RecipeTagsMapper {

    fun toModel(recipe: RecipeEntity): RecipeTags {
        return RecipeTags(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            description = recipe.description,
            tagNames = recipe.tags.map { TagName(it.name) }
        )
    }
}
