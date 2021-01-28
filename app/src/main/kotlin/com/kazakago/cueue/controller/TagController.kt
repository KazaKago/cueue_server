package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagName
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.repository.TagRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagController(private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName) {
        val tag = tagRepository.getTag(tagName)
        call.respond(HttpStatusCode.OK, tag)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName, tag: TagRegistrationData) {
        tagRepository.updateTag(tagName, tag)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName) {
        tagRepository.deleteTag(tagName)
        call.respond(HttpStatusCode.NoContent)
    }
}
