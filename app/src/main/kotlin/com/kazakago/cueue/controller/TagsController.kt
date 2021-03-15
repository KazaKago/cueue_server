package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagsController(private val userRepository: UserRepository, private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val user = userRepository.getUser(firebaseUser.uid)
        val models = tagRepository.getTags(user.personalWorkspace.id)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, tag: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = tagRepository.createTag(user.personalWorkspace.id, tag)
        call.respond(HttpStatusCode.Created, model)
    }
}
