package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.repository.MenuRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenusController(private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, afterId: MenuId?) {
        val menus = menuRepository.getMenus(afterId)
        call.respond(HttpStatusCode.OK, menus)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, menu: MenuRegistrationData) {
        menuRepository.createMenu(menu)
        call.respond(HttpStatusCode.Created)
    }
}
