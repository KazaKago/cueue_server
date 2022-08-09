package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.model.ContentSerializer
import com.kazakago.cueue.model.UserId
import com.kazakago.cueue.model.UserSummary

class UserSummaryMapper {

    fun toModel(user: UserEntity): UserSummary {
        return UserSummary(
            id = UserId(user.id.value),
            displayName = user.displayName,
            photo = user.photo?.let { ContentSerializer(key = it.key) },
        )
    }
}
