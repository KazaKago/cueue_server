package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.repository.MenuRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenuController(private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId) {
        val menu = menuRepository.getMenu(menuId)
        call.respond(HttpStatusCode.OK, menu)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId, menu: MenuRegistrationData) {
        menuRepository.updateMenu(menuId, menu)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId) {
        menuRepository.deleteMenu(menuId)
        call.respond(HttpStatusCode.NoContent)
    }
}
