package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.model.Recipe
import com.kazakago.cueue.model.RecipeId

class RecipeMapper(private val tagMapper: TagMapper) {

    fun toModel(recipe: RecipeEntity): Recipe {
        return Recipe(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            description = recipe.description,
            tags = recipe.tags.map { tagMapper.toModel(it) }
        )
    }
}
