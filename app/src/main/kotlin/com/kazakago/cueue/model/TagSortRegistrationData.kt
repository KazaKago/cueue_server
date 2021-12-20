package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagSortRegistrationData(
    @SerialName("tag_ids")
    val tagIds: List<TagId>,
)
