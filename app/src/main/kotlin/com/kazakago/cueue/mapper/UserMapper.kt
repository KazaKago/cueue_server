package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.model.User
import com.kazakago.cueue.model.UserId

class UserMapper(private val workspaceMapper: WorkspaceMapper) {

    fun toModel(user: UserEntity): User {
        val personalWorkspace = user.personalWorkSpace()
        return User(
            id = UserId(user.id.value),
            personalWorkspace = workspaceMapper.toModel(personalWorkspace),
        )
    }
}
