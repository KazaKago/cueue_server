package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UsersController(private val userRepository: UserRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val model = userRepository.getUser(firebaseUser.uid)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser) {
        if (!userRepository.existUser(firebaseUser.uid)) {
            userRepository.createUser(firebaseUser.uid)
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.OK)
        }
    }
}
