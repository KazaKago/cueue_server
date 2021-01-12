package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.model.TagRegistrationData
import com.kazakago.cooking_planner.repository.TagRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagsController(private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall) {
        val recipes = tagRepository.getTagList()
        call.respond(HttpStatusCode.OK, recipes)
    }

    suspend fun create(call: ApplicationCall, tag: TagRegistrationData) {
        tagRepository.createTag(tag)
        call.respond(HttpStatusCode.Created)
    }
}
