package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.repository.InvitationRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class InvitationAcceptController(private val invitationRepository: InvitationRepository, private val userRepository: UserRepository) {

    suspend fun accept(call: ApplicationCall, firebaseUser: FirebaseUser, invitationCode: InvitationCode) {
        val model = invitationRepository.getInvitation(invitationCode)
        userRepository.updateWorkspace(firebaseUser.uid, model.workspace.id)
        invitationRepository.deleteInvitation(invitationCode)
        call.respond(HttpStatusCode.OK)
    }
}
