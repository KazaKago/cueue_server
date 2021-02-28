package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.model.Recipe
import com.kazakago.cueue.model.RecipeId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeMapper(private val tagMapper: TagMapper) {

    suspend fun toModel(recipe: RecipeEntity): Recipe {
        return Recipe(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            description = recipe.description,
            image = recipe.createImageUrl().toString(),
            tags = newSuspendedTransaction { recipe.tags.map { tagMapper.toModel(it) } }
        )
    }
}
