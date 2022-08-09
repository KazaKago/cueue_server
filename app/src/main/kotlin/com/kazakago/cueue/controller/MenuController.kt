package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class MenuController(private val userRepository: UserRepository, private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val menu = menuRepository.getMenu(user.requireWorkspace().id, menuId)
        call.respond(HttpStatusCode.OK, menu)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId, menu: MenuRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val menu = menuRepository.updateMenu(user.requireWorkspace().id, menuId, menu)
        call.respond(HttpStatusCode.OK, menu)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId) {
        val user = userRepository.getUser(firebaseUser.uid)
        menuRepository.deleteMenu(user.requireWorkspace().id, menuId)
        call.respond(HttpStatusCode.NoContent)
    }
}
