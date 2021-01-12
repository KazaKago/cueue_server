package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.model.TagName
import com.kazakago.cooking_planner.repository.TagRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagController(private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, tagName: TagName) {
        val recipe = tagRepository.getTagRecipes(tagName)
        call.respond(HttpStatusCode.OK, recipe)
    }

    suspend fun delete(call: ApplicationCall, tagName: TagName) {
        tagRepository.deleteTag(tagName)
        call.respond(HttpStatusCode.NoContent)
    }
}
