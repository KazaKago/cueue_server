package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.repository.InvitationRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class InvitationAcceptController(private val invitationRepository: InvitationRepository, private val userRepository: UserRepository) {

    suspend fun accept(call: ApplicationCall, firebaseUser: FirebaseUser, invitationCode: InvitationCode) {
        val invitation = invitationRepository.getInvitation(invitationCode)
        val user = userRepository.updateWorkspace(firebaseUser.uid, invitation.workspace.id)
        invitationRepository.deleteInvitation(invitationCode)
        call.respond(HttpStatusCode.OK, user)
    }
}
