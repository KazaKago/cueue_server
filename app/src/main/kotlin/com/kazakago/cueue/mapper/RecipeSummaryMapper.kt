package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.table.ContentsTable
import com.kazakago.cueue.database.table.MenusTable
import com.kazakago.cueue.model.ContentSerializer
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeSummary
import org.jetbrains.exposed.sql.SortOrder

class RecipeSummaryMapper {

    fun toModel(recipe: RecipeEntity): RecipeSummary {
        return RecipeSummary(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            image = recipe.images
                .orderBy(ContentsTable.recipeOrder to SortOrder.ASC)
                .limit(1)
                .firstNotNullOfOrNull { ContentSerializer(key = it.key) },
            lastCookingAt = recipe.menus
                .orderBy(MenusTable.date to SortOrder.DESC)
                .limit(1)
                .firstNotNullOfOrNull { it.date },
        )
    }
}
