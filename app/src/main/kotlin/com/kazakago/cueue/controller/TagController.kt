package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.TagMapper
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagName
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagController(private val userRepository: UserRepository, private val tagRepository: TagRepository, private val tagMapper: TagMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName) {
        val user = userRepository.getUser(firebaseUser.uid)
        val tag = tagRepository.getTag(user.defaultWorkSpace(), tagName)
        call.respond(HttpStatusCode.OK, tagMapper.toModel(tag))
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName, tag: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        tagRepository.updateTag(user.defaultWorkSpace(), tagName, tag)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName) {
        val user = userRepository.getUser(firebaseUser.uid)
        tagRepository.deleteTag(user.defaultWorkSpace(), tagName)
        call.respond(HttpStatusCode.NoContent)
    }
}
