package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.model.TagSortRegistrationData
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class TagsController(private val userRepository: UserRepository, private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val user = userRepository.getUser(firebaseUser.uid)
        val models = tagRepository.getTags(user.requireWorkspace().id)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, tag: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = tagRepository.createTag(user.requireWorkspace().id, tag)
        call.respond(HttpStatusCode.Created, model)
    }

    suspend fun order(call: ApplicationCall, firebaseUser: FirebaseUser, tagSort: TagSortRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = tagRepository.updateOrder(user.requireWorkspace().id, tagSort)
        call.respond(HttpStatusCode.OK, model)
    }
}
