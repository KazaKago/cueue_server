package com.kazakago.cueue.controller

import com.kazakago.cueue.model.ContentRegistration
import com.kazakago.cueue.model.ContentSerializer
import com.kazakago.cueue.model.DecodedImage
import com.kazakago.cueue.repository.ContentRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class ContentsController(private val contentRepository: ContentRepository) {

    suspend fun create(call: ApplicationCall, content: ContentRegistration) {
        val decodedImage = DecodedImage(content.data)
        val contentKey = contentRepository.create(decodedImage)
        call.respond(HttpStatusCode.Created, ContentSerializer(contentKey))
    }
}
