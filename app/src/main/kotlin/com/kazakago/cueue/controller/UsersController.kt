package com.kazakago.cueue.controller

import com.kazakago.cueue.model.UID
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class UsersController(private val userRepository: UserRepository) {

    suspend fun index(call: ApplicationCall) {
        val users = userRepository.getUsers()
        call.respond(HttpStatusCode.OK, users)
    }

    suspend fun create(call: ApplicationCall, uid: UID) {
//        userRepository.createUser(user)
        call.respond(HttpStatusCode.Created)
    }
}
