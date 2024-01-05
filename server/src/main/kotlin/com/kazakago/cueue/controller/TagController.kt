package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class TagController(private val userRepository: UserRepository, private val tagRepository: TagRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val tag = tagRepository.getTag(user.requireWorkspace().id, tagId)
        call.respond(HttpStatusCode.OK, tag)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId, tagRegistrationData: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val tag = tagRepository.updateTag(user.requireWorkspace().id, tagId, tagRegistrationData)
        call.respond(HttpStatusCode.OK, tag)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId) {
        val user = userRepository.getUser(firebaseUser.uid)
        tagRepository.deleteTag(user.requireWorkspace().id, tagId)
        call.respond(HttpStatusCode.NoContent)
    }
}
