package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.model.TagSortRegistrationData
import com.kazakago.cueue.model.UnsafeWorkspaceId
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class TagsController(private val userRepository: UserRepository, private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, unsafeWorkspaceId: UnsafeWorkspaceId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val workspaceId = unsafeWorkspaceId.validate(user)
        val models = tagRepository.getTags(workspaceId)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, unsafeWorkspaceId: UnsafeWorkspaceId, tag: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val workspaceId = unsafeWorkspaceId.validate(user)
        val model = tagRepository.createTag(workspaceId, tag)
        call.respond(HttpStatusCode.Created, model)
    }

    suspend fun order(call: ApplicationCall, firebaseUser: FirebaseUser, unsafeWorkspaceId: UnsafeWorkspaceId, tagSort: TagSortRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val workspaceId = unsafeWorkspaceId.validate(user)
        val model = tagRepository.updateOrder(workspaceId, tagSort)
        call.respond(HttpStatusCode.OK, model)
    }
}
