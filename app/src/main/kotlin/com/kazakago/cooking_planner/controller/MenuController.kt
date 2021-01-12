package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.model.MenuId
import com.kazakago.cooking_planner.repository.MenuRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenuController(private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall, menuId: MenuId) {
        val menu = menuRepository.getMenu(menuId)
        call.respond(HttpStatusCode.OK, menu)
    }

    suspend fun delete(call: ApplicationCall, menuId: MenuId) {
        menuRepository.deleteMenu(menuId)
        call.respond(HttpStatusCode.NoContent)
    }
}
