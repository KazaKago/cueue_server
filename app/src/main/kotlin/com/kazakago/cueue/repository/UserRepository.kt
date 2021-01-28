package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.UsersTable
import com.kazakago.cueue.mapper.UserMapper
import com.kazakago.cueue.model.UID
import com.kazakago.cueue.model.User
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepository(private val userMapper: UserMapper) {

    suspend fun getUser(uid: UID): User {
        return newSuspendedTransaction(db = DbSettings.db) {
            val user = UserEntity.find { UsersTable.uid eq uid.value }.first()
            userMapper.toModel(user)
        }
    }

    suspend fun existUser(uid: UID): Boolean {
        return newSuspendedTransaction(db = DbSettings.db) {
            UserEntity.find { UsersTable.uid eq uid.value }.empty().not()
        }
    }

    suspend fun createUser(_uid: UID): User {
        return newSuspendedTransaction(db = DbSettings.db) {
            UserEntity.new {
                uid = _uid.value
            }
        }.let {
            userMapper.toModel(it)
        }
    }
}
