package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class MenusController(private val userRepository: UserRepository, private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser) {
        val user = userRepository.getUser(firebaseUser.uid)
        val menus = menuRepository.getMenus(user.requireWorkspace().id)
        call.respond(HttpStatusCode.OK, menus)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, menuRegistrationData: MenuRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val menu = menuRepository.createMenu(user.requireWorkspace().id, menuRegistrationData)
        call.respond(HttpStatusCode.Created, menu)
    }
}
