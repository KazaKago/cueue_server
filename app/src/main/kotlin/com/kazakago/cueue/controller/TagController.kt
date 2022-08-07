package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class TagController(private val userRepository: UserRepository, private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val models = tagRepository.getTag(user.requireWorkspace().id, tagId)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId, tag: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = tagRepository.updateTag(user.requireWorkspace().id, tagId, tag)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId) {
        val user = userRepository.getUser(firebaseUser.uid)
        tagRepository.deleteTag(user.requireWorkspace().id, tagId)
        call.respond(HttpStatusCode.NoContent)
    }
}
