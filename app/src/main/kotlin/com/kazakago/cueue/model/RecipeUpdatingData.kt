package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeUpdatingData(
    @SerialName("title")
    val title: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("image_data_uri")
    val imageDataUri: String? = null,
    @SerialName("tag_ids")
    val tagIds: List<TagId>? = null,
) {
    val decodedImage: DecodedImage? by lazy { imageDataUri?.let { DecodedImage(it) } }
}
