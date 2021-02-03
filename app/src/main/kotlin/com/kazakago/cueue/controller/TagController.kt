package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.TagMapper
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.TagUpdatingData
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class TagController(private val userRepository: UserRepository, private val tagRepository: TagRepository, private val tagMapper: TagMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val entity = tagRepository.getTag(user.personalWorkSpace(), tagId)
        call.respond(HttpStatusCode.OK, tagMapper.toModel(entity))
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId, tag: TagUpdatingData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val entity = tagRepository.updateTag(user.personalWorkSpace(), tagId, tag)
        call.respond(HttpStatusCode.OK, tagMapper.toModel(entity))
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, tagId: TagId) {
        val user = userRepository.getUser(firebaseUser.uid)
        tagRepository.deleteTag(user.personalWorkSpace(), tagId)
        call.respond(HttpStatusCode.NoContent)
    }
}
