package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeRegistrationData(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("image_data")
    val imageData: String? = null,
    @SerialName("tag_ids")
    val tagIds: List<TagId>? = null,
) {
    val decodedImage: DecodedImage? by lazy { imageData?.let { DecodedImage(it) } }
}
