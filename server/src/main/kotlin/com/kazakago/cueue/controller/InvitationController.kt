package com.kazakago.cueue.controller

import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.repository.InvitationRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class InvitationController(private val invitationRepository: InvitationRepository) {

    suspend fun index(call: ApplicationCall, invitationCode: InvitationCode) {
        val invitation = invitationRepository.getInvitation(invitationCode)
        call.respond(HttpStatusCode.OK, invitation)
    }
}
