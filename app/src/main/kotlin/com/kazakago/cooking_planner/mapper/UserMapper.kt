package com.kazakago.cooking_planner.mapper

import com.kazakago.cooking_planner.database.entity.UserEntity
import com.kazakago.cooking_planner.model.Email
import com.kazakago.cooking_planner.model.User
import com.kazakago.cooking_planner.model.UserId

class UserMapper {

    fun toModel(user: UserEntity): User {
        return User(
            id = UserId(user.id.value),
            email = Email(user.email),
        )
    }
}
