package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UserController(private val userRepository: UserRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val user = userRepository.getUser(firebaseUser.uid)
        call.respond(HttpStatusCode.OK, user)
    }
}
