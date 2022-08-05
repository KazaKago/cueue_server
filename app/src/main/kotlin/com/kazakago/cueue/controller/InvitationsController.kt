package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.model.UnsafeWorkspaceId
import com.kazakago.cueue.repository.InvitationRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class InvitationsController(private val userRepository: UserRepository, private val invitationRepository: InvitationRepository) {

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, unsafeWorkspaceId: UnsafeWorkspaceId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val workspaceId = unsafeWorkspaceId.validate(user)
        val code = InvitationCode.generate()
        invitationRepository.createInvitation(user.id, workspaceId, code)
        call.respond(HttpStatusCode.Created, code.value)
    }
}