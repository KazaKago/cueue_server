package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.WorkspaceRegistrationData
import com.kazakago.cueue.repository.UserRepository
import com.kazakago.cueue.repository.WorkspaceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class WorkspacesController(private val userRepository: UserRepository, private val workspaceRepository: WorkspaceRepository) {

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceRegistrationData: WorkspaceRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        if (user.workspace == null) {
            val workspace = workspaceRepository.createWorkspace(workspaceRegistrationData)
            val updatedUser = userRepository.updateWorkspace(firebaseUser.uid, workspace.id)
            call.respond(HttpStatusCode.Created, updatedUser.requireWorkspace())
        } else {
            call.respond(HttpStatusCode.Conflict)
        }
    }
}
