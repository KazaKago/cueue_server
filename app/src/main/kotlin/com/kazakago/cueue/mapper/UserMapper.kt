package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.model.ContentSerializer
import com.kazakago.cueue.model.User
import com.kazakago.cueue.model.UserId

class UserMapper(private val workspaceMapper: WorkspaceMapper) {

    fun toModel(user: UserEntity): User {
        return User(
            id = UserId(user.id.value),
            displayName = user.displayName,
            photo = user.photo?.let { ContentSerializer(key = it.key) },
            workspace = user.workspace?.let { workspaceMapper.toModel(it) },
        )
    }
}
