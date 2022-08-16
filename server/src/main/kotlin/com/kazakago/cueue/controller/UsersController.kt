package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.UserRegistrationData
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class UsersController(private val userRepository: UserRepository) {

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, userRegistrationData: UserRegistrationData) {
        if (!userRepository.existUser(firebaseUser.uid)) {
            val user = userRepository.createUser(firebaseUser.uid, userRegistrationData)
            call.respond(HttpStatusCode.Created, user)
        } else {
            call.respond(HttpStatusCode.Conflict)
        }
    }
}
