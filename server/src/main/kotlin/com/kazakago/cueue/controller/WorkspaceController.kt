package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.WorkspaceRegistrationData
import com.kazakago.cueue.repository.UserRepository
import com.kazakago.cueue.repository.WorkspaceRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class WorkspaceController(private val userRepository: UserRepository, private val workspaceRepository: WorkspaceRepository) {

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceRegistrationData: WorkspaceRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val workspace = workspaceRepository.updateWorkspace(user.requireWorkspace().id, workspaceRegistrationData)
        call.respond(HttpStatusCode.OK, workspace)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val user = userRepository.getUser(firebaseUser.uid)
        if (user.workspace != null) {
            userRepository.updateWorkspace(firebaseUser.uid, null)
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
