package com.kazakago.cueue.mapper

import com.google.cloud.storage.Bucket
import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.model.Recipe
import com.kazakago.cueue.model.RecipeId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class RecipeMapper(private val tagMapper: TagMapper, private val bucket: Bucket) {

    suspend fun toModel(recipe: RecipeEntity): Recipe {
        return Recipe(
            id = RecipeId(recipe.id.value),
            title = recipe.title,
            description = recipe.description,
            image = recipe.createImageUrl(bucket)?.toString(),
            tags = newSuspendedTransaction { recipe.tags.map { tagMapper.toModel(it) } }
        )
    }
}
