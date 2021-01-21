package com.kazakago.cueue.mapper

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.model.Email
import com.kazakago.cueue.model.User
import com.kazakago.cueue.model.UserId

class UserMapper {

    fun toModel(user: UserEntity): User {
        return User(
            id = UserId(user.id.value),
            email = Email(user.email),
            nickname = user.nickname,
        )
    }
}
