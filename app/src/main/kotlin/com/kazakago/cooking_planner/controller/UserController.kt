package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.model.UserId
import com.kazakago.cooking_planner.model.UserRegistrationData
import com.kazakago.cooking_planner.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class UserController(private val userRepository: UserRepository) {

    suspend fun index(call: ApplicationCall, userId: UserId) {
        val tag = userRepository.getUser(userId)
        call.respond(HttpStatusCode.OK, tag)
    }

    suspend fun update(call: ApplicationCall, userId: UserId, user: UserRegistrationData) {
        userRepository.updateUser(userId, user)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun delete(call: ApplicationCall, userId: UserId) {
        userRepository.deleteUser(userId)
        call.respond(HttpStatusCode.NoContent)
    }
}
