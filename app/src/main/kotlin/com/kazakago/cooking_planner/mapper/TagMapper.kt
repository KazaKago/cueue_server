package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.database.entity.TagEntity
import com.kazakago.cooking_planner.model.Tag
import com.kazakago.cooking_planner.model.TagName

class TagMapper {

    fun toModel(tag: TagEntity): Tag {
        return Tag(
            name = TagName(tag.name),
        )
    }
}
