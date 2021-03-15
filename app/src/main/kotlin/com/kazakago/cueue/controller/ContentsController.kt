package com.kazakago.cueue.controller

import com.kazakago.cueue.model.DecodedImage
import com.kazakago.cueue.model.Image
import com.kazakago.cueue.model.ImageRegistration
import com.kazakago.cueue.repository.ContentRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class ContentsController(private val contentRepository: ContentRepository) {

    suspend fun createImage(call: ApplicationCall, image: ImageRegistration) {
        val decodedImage = DecodedImage(image.data)
        val imageKey = contentRepository.createImage(decodedImage)
        call.respond(HttpStatusCode.Created, Image(imageKey))
    }
}
