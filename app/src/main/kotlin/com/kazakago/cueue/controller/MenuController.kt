package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.model.WorkspaceId
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenuController(private val userRepository: UserRepository, private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, menuId: MenuId) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        val model = menuRepository.getMenu(workspaceId, menuId)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, menuId: MenuId, menu: MenuRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        val model = menuRepository.updateMenu(workspaceId, menuId, menu)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, menuId: MenuId) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        menuRepository.deleteMenu(workspaceId, menuId)
        call.respond(HttpStatusCode.NoContent)
    }
}
