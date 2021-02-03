package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.model.Tag
import com.kazakago.cueue.model.TagId

class TagMapper {

    fun toModel(tag: TagEntity): Tag {
        return Tag(
            id = TagId(tag.id.value),
            name = tag.name,
        )
    }
}
