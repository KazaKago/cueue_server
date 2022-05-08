package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.table.UsersTable
import com.kazakago.cueue.mapper.UserMapper
import com.kazakago.cueue.model.UID
import com.kazakago.cueue.model.User
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepository(private val userMapper: UserMapper) {

    suspend fun getUser(uid: UID): User {
        return newSuspendedTransaction {
            val entity = UserEntity.find { UsersTable.uid eq uid.value }.first()
            userMapper.toModel(entity)
        }
    }

    suspend fun existUser(uid: UID): Boolean {
        return newSuspendedTransaction {
            UserEntity.find { UsersTable.uid eq uid.value }.empty().not()
        }
    }

    suspend fun createUser(uid: UID): User {
        return newSuspendedTransaction {
            val userEntity = UserEntity.new {
                this.uid = uid.value
            }
            userMapper.toModel(userEntity)
        }
    }

    suspend fun deleteUser(uid: UID) {
        return newSuspendedTransaction {
            val user = UserEntity
                .find { UsersTable.uid eq uid.value }
                .first()
            val workspaces = user.workspaces.copy()
            user.delete()
            workspaces.forEach { workspace ->
                if (workspace.users.empty()) {
                    workspace.delete()
                }
            }
        }
    }
}
