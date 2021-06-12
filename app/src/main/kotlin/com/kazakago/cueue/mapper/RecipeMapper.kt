package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.table.ContentsTable
import com.kazakago.cueue.database.table.MenusTable
import com.kazakago.cueue.model.ContentSerializer
import com.kazakago.cueue.model.Recipe
import com.kazakago.cueue.model.RecipeId
import org.jetbrains.exposed.sql.SortOrder

class RecipeMapper(private val tagMapper: TagMapper) {

    fun toModel(recipe: RecipeEntity): Recipe {
        return Recipe(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            description = recipe.description,
            url = recipe.url,
            images = recipe.images
                .orderBy(ContentsTable.recipeOrder to SortOrder.ASC)
                .map { ContentSerializer(key = it.key) },
            tags = recipe.tags.map { tagMapper.toModel(it) },
            cookingHistories = recipe.menus
                .orderBy(MenusTable.date to SortOrder.DESC)
                .limit(5)
                .map { it.date.toString() },
            cookingCount = recipe.menus.count(),
            createdAt = recipe.createdAt,
            updatedAt = recipe.updatedAt,
        )
    }
}
