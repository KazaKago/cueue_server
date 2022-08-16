package com.kazakago.cueue.controller

import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.repository.InvitationRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class InvitationController(private val invitationRepository: InvitationRepository) {

    suspend fun index(call: ApplicationCall, invitationCode: InvitationCode) {
        val invitation = invitationRepository.getInvitation(invitationCode)
        call.respond(HttpStatusCode.OK, invitation)
    }
}
