package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.data.database.entity.TagEntity
import com.kazakago.cooking_planner.domain.model.Tag
import com.kazakago.cooking_planner.domain.model.TagImpl
import com.kazakago.cooking_planner.domain.model.TagName

class TagMapper {

    fun toModel(tag: TagEntity): Tag {
        return TagImpl(
            name = TagName(tag.name),
        )
    }
}
