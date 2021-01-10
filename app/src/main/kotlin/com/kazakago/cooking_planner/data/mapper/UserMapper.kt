package com.kazakago.cooking_planner.data.mapper

import com.kazakago.cooking_planner.data.database.entity.UserEntity
import com.kazakago.cooking_planner.domain.model.Email
import com.kazakago.cooking_planner.domain.model.User
import com.kazakago.cooking_planner.domain.model.UserId

class UserMapper {

    fun toModel(user: UserEntity): User {
        return User(
            id = UserId(user.id.value),
            email = Email(user.email),
        )
    }
}
