package com.kazakago.cooking_planner.presentation.controller

import com.kazakago.cooking_planner.domain.model.TagName
import com.kazakago.cooking_planner.data.repository.TagRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagController(private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, tagName: String) {
        val recipe = tagRepository.getTagRecipes(TagName(tagName))
        call.respond(HttpStatusCode.OK, recipe)
    }

    suspend fun delete(call: ApplicationCall, tagName: String) {
        tagRepository.deleteTag(TagName(tagName))
        call.respond(HttpStatusCode.NoContent)
    }
}
