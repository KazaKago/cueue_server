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
        val tags = tagRepository.getTags(user.requireWorkspace().id)
        call.respond(HttpStatusCode.OK, tags)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, tagRegistrationData: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val tag = tagRepository.createTag(user.requireWorkspace().id, tagRegistrationData)
        call.respond(HttpStatusCode.Created, tag)
    }

    suspend fun order(call: ApplicationCall, firebaseUser: FirebaseUser, tagSort: TagSortRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val tags = tagRepository.updateOrder(user.requireWorkspace().id, tagSort)
        call.respond(HttpStatusCode.OK, tags)
    }
}
