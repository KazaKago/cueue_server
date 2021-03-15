package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenusController(private val userRepository: UserRepository, private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, afterId: MenuId?) {
        val user = userRepository.getUser(firebaseUser.uid)
        val models = menuRepository.getMenus(user.personalWorkspace.id, afterId)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, menu: MenuRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = menuRepository.createMenu(user.personalWorkspace.id, menu)
        call.respond(HttpStatusCode.Created, model)
    }
}
