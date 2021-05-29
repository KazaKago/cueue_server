package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class UsersController(private val userRepository: UserRepository) {

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser) {
        if (!userRepository.existUser(firebaseUser.uid)) {
            userRepository.createUser(firebaseUser.uid)
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.OK)
        }
    }
}
