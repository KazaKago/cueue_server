package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.model.ContentSerializer
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeSummary

class RecipeSummaryMapper {

    fun toModel(recipe: RecipeEntity): RecipeSummary {
        return RecipeSummary(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            image = recipe.images
                .sortedBy { it.recipeOrder }
                .firstNotNullOfOrNull { ContentSerializer(key = it.key) },
            lastCookingAt = recipe.menus
                .sortedByDescending { it.date }
                .firstNotNullOfOrNull { it.date },
        )
    }
}
