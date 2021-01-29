package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagUpdatingData(
    @SerialName("name")
    val name: TagName? = null,
    @SerialName("recipe_ids")
    val recipeIds: List<RecipeId>? = null,
)
