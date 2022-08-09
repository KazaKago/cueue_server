package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class MenusController(private val userRepository: UserRepository, private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, afterId: MenuId?) {
        val user = userRepository.getUser(firebaseUser.uid)
        val menus = menuRepository.getMenus(user.requireWorkspace().id, afterId)
        call.respond(HttpStatusCode.OK, menus)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, menuRegistrationData: MenuRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val menu = menuRepository.createMenu(user.requireWorkspace().id, menuRegistrationData)
        call.respond(HttpStatusCode.Created, menu)
    }
}
