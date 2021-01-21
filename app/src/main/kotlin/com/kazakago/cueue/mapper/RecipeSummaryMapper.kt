package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.RecipeSummaryEntity
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeSummary

class RecipeSummaryMapper {

    fun toModel(recipe: RecipeSummaryEntity): RecipeSummary {
        return RecipeSummary(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
        )
    }
}
