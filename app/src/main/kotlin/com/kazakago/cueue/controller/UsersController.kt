package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.repository.UserRepository
import com.kazakago.cueue.repository.WorkspaceRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class UsersController(private val userRepository: UserRepository, private val workspaceRepository: WorkspaceRepository) {

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser) {
        if (!userRepository.existUser(firebaseUser.uid)) {
            val user = userRepository.createUser(firebaseUser.uid)
            workspaceRepository.createWorkspace(user)
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.OK)
        }
    }
}
