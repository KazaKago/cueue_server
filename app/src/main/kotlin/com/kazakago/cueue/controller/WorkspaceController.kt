package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.UnsafeWorkspaceId
import com.kazakago.cueue.model.WorkspaceRegistrationData
import com.kazakago.cueue.repository.UserRepository
import com.kazakago.cueue.repository.WorkspaceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class WorkspaceController(private val userRepository: UserRepository, private val workspaceRepository: WorkspaceRepository) {

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, unsafeWorkspaceId: UnsafeWorkspaceId, workspaceRegistrationData: WorkspaceRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val workspaceId = unsafeWorkspaceId.validate(user)
        val model = workspaceRepository.updateWorkspace(workspaceId, workspaceRegistrationData)
        call.respond(HttpStatusCode.OK, model)
    }
}
