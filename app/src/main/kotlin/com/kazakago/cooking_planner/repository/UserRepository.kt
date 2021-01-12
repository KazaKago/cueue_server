package com.kazakago.cooking_planner.repository

import com.kazakago.cooking_planner.database.entity.UserEntity
import com.kazakago.cooking_planner.database.setting.DbSettings
import com.kazakago.cooking_planner.mapper.UserMapper
import com.kazakago.cooking_planner.model.User
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
