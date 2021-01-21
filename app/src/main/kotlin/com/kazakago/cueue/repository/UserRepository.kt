package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.mapper.UserMapper
import com.kazakago.cueue.model.User
import com.kazakago.cueue.model.UserId
import com.kazakago.cueue.model.UserRegistrationData
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class UserRepository(private val userMapper: UserMapper) {

    suspend fun getUsers(): List<User> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val users = UserEntity.all()
            users.map { userMapper.toModel(it) }
        }
    }

    suspend fun getUser(userId: UserId): User {
        return newSuspendedTransaction(db = DbSettings.db) {
            val user = UserEntity[userId.value]
            userMapper.toModel(user)
        }
    }

    suspend fun createUser(user: UserRegistrationData) {
        newSuspendedTransaction(db = DbSettings.db) {
            UserEntity.new {
                email = user.email.value
                nickname = user.nickname
            }
        }
    }

    suspend fun updateUser(userId: UserId, user: UserRegistrationData) {
        newSuspendedTransaction(db = DbSettings.db) {
            UserEntity[userId.value].apply {
                email = user.email.value
                nickname = user.nickname
                updatedAt = LocalDateTime.now()
            }
        }
    }

    suspend fun deleteUser(userId: UserId) {
        newSuspendedTransaction(db = DbSettings.db) {
            val user = UserEntity[userId.value]
            user.delete()
        }
    }
}
