package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.TagEntity
import com.kazakago.cueue.model.Tag
import com.kazakago.cueue.model.TagName

class TagMapper {

    suspend fun toModel(tag: TagEntity): Tag {
        return Tag(
            name = TagName(tag.name),
        )
    }
}
