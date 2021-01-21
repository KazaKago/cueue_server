package com.kazakago.cueue.controller

import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.repository.TagRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagsController(private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall) {
        val tags = tagRepository.getTags()
        call.respond(HttpStatusCode.OK, tags)
    }

    suspend fun create(call: ApplicationCall, tag: TagRegistrationData) {
        tagRepository.createTag(tag)
        call.respond(HttpStatusCode.Created)
    }
}
