package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.UsersTable
import com.kazakago.cueue.database.table.WorkspacesTable
import com.kazakago.cueue.mapper.UserMapper
import com.kazakago.cueue.mapper.rawValue
import com.kazakago.cueue.model.UID
import com.kazakago.cueue.model.User
import com.kazakago.cueue.model.WorkspaceType
import org.jetbrains.exposed.sql.SizedCollection
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
            }.apply {
                this.workspaces = SizedCollection(createWorkspace())
            }
            userMapper.toModel(userEntity)
        }
    }

    private fun createWorkspace(): WorkspaceEntity {
        return WorkspaceEntity.new {
            this.name = WorkspacesTable.PERSONAL_DEFAULT_NAME
            this.type = WorkspaceType.Personal.rawValue()
        }
    }
}
