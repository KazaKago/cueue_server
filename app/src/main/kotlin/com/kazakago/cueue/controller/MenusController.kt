package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.MenuMapper
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenusController(private val userRepository: UserRepository, private val menuRepository: MenuRepository, private val menuMapper: MenuMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, afterId: MenuId?) {
        val user = userRepository.getUser(firebaseUser.uid)
        val entities = menuRepository.getMenus(user.personalWorkSpace(), afterId)
        call.respond(HttpStatusCode.OK, entities.map { menuMapper.toModel(it) })
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, menu: MenuRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val entity = menuRepository.createMenu(user.personalWorkSpace(), menu)
        call.respond(HttpStatusCode.Created, menuMapper.toModel(entity))
    }
}
