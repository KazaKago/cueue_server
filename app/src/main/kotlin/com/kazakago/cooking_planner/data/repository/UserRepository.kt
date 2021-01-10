package com.kazakago.cooking_planner.data.repository

import com.kazakago.cooking_planner.data.database.entity.UserEntity
import com.kazakago.cooking_planner.data.database.setting.DbSettings
import com.kazakago.cooking_planner.data.mapper.UserMapper
import com.kazakago.cooking_planner.domain.model.User
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepository(private val userMapper: UserMapper) {

    suspend fun getUsers(): List<User> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val users = UserEntity.all()
            users.map { userMapper.toModel(it) }
        }
    }

    suspend fun createUser() {
        newSuspendedTransaction(db = DbSettings.db) {
            UserEntity.new {
                email = "hogehoge@gmail.com"
            }
        }
    }
}
