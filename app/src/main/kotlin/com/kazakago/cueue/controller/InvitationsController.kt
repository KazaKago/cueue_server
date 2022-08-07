package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.repository.InvitationRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class InvitationsController(private val userRepository: UserRepository, private val invitationRepository: InvitationRepository) {

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val user = userRepository.getUser(firebaseUser.uid)
        val code = InvitationCode.generate()
        val model = invitationRepository.createInvitation(user.id, user.requireWorkspace().id, code)
        call.respond(HttpStatusCode.Created, model)
    }
}
