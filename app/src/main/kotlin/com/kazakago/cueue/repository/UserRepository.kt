package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.UsersTable
import com.kazakago.cueue.model.UID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepository {

    suspend fun getUser(uid: UID): UserEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            UserEntity.find { UsersTable.uid eq uid.value }.first()
        }
    }

    suspend fun existUser(uid: UID): Boolean {
        return newSuspendedTransaction(db = DbSettings.db) {
            UserEntity.find { UsersTable.uid eq uid.value }.empty().not()
        }
    }

    suspend fun createUser(uid: UID): UserEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            UserEntity.new {
                this.uid = uid.value
            }
        }
    }
}
