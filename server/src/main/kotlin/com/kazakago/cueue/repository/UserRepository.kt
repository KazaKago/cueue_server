package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.ContentEntity
import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.ContentsTable
import com.kazakago.cueue.database.table.UsersTable
import com.kazakago.cueue.mapper.UserMapper
import com.kazakago.cueue.model.UID
import com.kazakago.cueue.model.User
import com.kazakago.cueue.model.UserRegistrationData
import com.kazakago.cueue.model.WorkspaceId
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

    suspend fun createUser(uid: UID, user: UserRegistrationData): User {
        return newSuspendedTransaction {
            val entity = UserEntity.new {
                this.uid = uid.value
                this.displayName = user.displayName
                this.photo = user.photoKey?.let { ContentEntity.find { ContentsTable.key eq user.photoKey }.first() }
                this.workspace = WorkspaceEntity.new {}
            }
            userMapper.toModel(entity)
        }
    }

    suspend fun updateUser(uid: UID, user: UserRegistrationData): User {
        return newSuspendedTransaction {
            val entity = UserEntity.find { UsersTable.uid eq uid.value }
                .first()
                .apply {
                    this.displayName = user.displayName
                    this.photo = user.photoKey?.let { ContentEntity.find { ContentsTable.key eq user.photoKey }.first() }
                }
            userMapper.toModel(entity)
        }
    }

    suspend fun updateWorkspace(uid: UID, workspaceId: WorkspaceId): User {
        return newSuspendedTransaction {
            val entity = UserEntity.find { UsersTable.uid eq uid.value }
                .first()
                .apply {
                    this.workspace = WorkspaceEntity[workspaceId.value]
                }
            userMapper.toModel(entity)
        }
    }
}
