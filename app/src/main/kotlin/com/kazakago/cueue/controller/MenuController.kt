package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenuController(private val userRepository: UserRepository, private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = menuRepository.getMenu(user.personalWorkspace.id, menuId)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId, menu: MenuRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = menuRepository.updateMenu(user.personalWorkspace.id, menuId, menu)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId) {
        val user = userRepository.getUser(firebaseUser.uid)
        menuRepository.deleteMenu(user.personalWorkspace.id, menuId)
        call.respond(HttpStatusCode.NoContent)
    }
}
