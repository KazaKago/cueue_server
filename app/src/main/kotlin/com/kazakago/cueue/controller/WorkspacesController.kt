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
            val model = workspaceRepository.createWorkspace(workspaceRegistrationData)
            userRepository.updateWorkspace(firebaseUser.uid, model.id)
            call.respond(HttpStatusCode.Created, model)
        } else {
            call.respond(HttpStatusCode.Conflict)
        }
    }
}
