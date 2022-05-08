package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.WorkspaceRegistrationData
import com.kazakago.cueue.repository.UserRepository
import com.kazakago.cueue.repository.WorkspaceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class WorkspacesController(private val userRepository: UserRepository, private val workspaceRepository: WorkspaceRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = workspaceRepository.getWorkspaces(user.id)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceRegistrationData: WorkspaceRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = workspaceRepository.createWorkspace(user.id, workspaceRegistrationData)
        call.respond(HttpStatusCode.Created, model)
    }
}
