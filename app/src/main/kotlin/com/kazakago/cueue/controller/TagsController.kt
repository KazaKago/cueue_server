package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.TagMapper
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagRegistrationData
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagsController(private val userRepository: UserRepository, private val tagRepository: TagRepository, private val tagMapper: TagMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val user = userRepository.getUser(firebaseUser.uid)
        val tags = tagRepository.getTags(user.defaultWorkSpace())
        call.respond(HttpStatusCode.OK, tags.map { tagMapper.toModel(it) })
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, tag: TagRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        tagRepository.createTag(user.defaultWorkSpace(), tag)
        call.respond(HttpStatusCode.Created)
    }
}
