package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.model.WorkspaceId
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagsController(private val userRepository: UserRepository, private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        val models = tagRepository.getTags(workspaceId)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, tag: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        val model = tagRepository.createTag(workspaceId, tag)
        call.respond(HttpStatusCode.Created, model)
    }
}
