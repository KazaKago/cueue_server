package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class UserController(private val userRepository: UserRepository) {

    suspend fun index(call: ApplicationCall) {
        val users = userRepository.getUsers()
        call.respond(HttpStatusCode.OK, users)
    }

    suspend fun create(call: ApplicationCall) {
        userRepository.createUser()
        call.respond(HttpStatusCode.Created)
    }
}
