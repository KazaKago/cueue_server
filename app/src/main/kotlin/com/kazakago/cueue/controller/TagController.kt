package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.model.WorkspaceId
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagController(private val userRepository: UserRepository, private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, tagId: TagId) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        val models = tagRepository.getTag(workspaceId, tagId)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, tagId: TagId, tag: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        val model = tagRepository.updateTag(workspaceId, tagId, tag)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, tagId: TagId) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        tagRepository.deleteTag(workspaceId, tagId)
        call.respond(HttpStatusCode.NoContent)
    }
}
