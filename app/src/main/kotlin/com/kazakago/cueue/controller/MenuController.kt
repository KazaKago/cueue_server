package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.MenuMapper
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuUpdatingData
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenuController(private val userRepository: UserRepository, private val menuRepository: MenuRepository, private val menuMapper: MenuMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val entity = menuRepository.getMenu(user.personalWorkSpace(), menuId)
        call.respond(HttpStatusCode.OK, menuMapper.toModel(entity))
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId, menu: MenuUpdatingData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val entity = menuRepository.updateMenu(user.personalWorkSpace(), menuId, menu)
        call.respond(HttpStatusCode.OK, menuMapper.toModel(entity))
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, menuId: MenuId) {
        val user = userRepository.getUser(firebaseUser.uid)
        menuRepository.deleteMenu(user.personalWorkSpace(), menuId)
        call.respond(HttpStatusCode.NoContent)
    }
}
