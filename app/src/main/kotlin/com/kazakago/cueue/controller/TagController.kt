package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.TagMapper
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagName
import com.kazakago.cueue.model.TagUpdatingData
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagController(private val userRepository: UserRepository, private val tagRepository: TagRepository, private val tagMapper: TagMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName) {
        val user = userRepository.getUser(firebaseUser.uid)
        val tag = tagRepository.getTag(user.personalWorkSpace(), tagName)
        call.respond(HttpStatusCode.OK, tagMapper.toModel(tag))
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName, tag: TagUpdatingData) {
        val user = userRepository.getUser(firebaseUser.uid)
        tagRepository.updateTag(user.personalWorkSpace(), tagName, tag)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, tagName: TagName) {
        val user = userRepository.getUser(firebaseUser.uid)
        tagRepository.deleteTag(user.personalWorkSpace(), tagName)
        call.respond(HttpStatusCode.NoContent)
    }
}
