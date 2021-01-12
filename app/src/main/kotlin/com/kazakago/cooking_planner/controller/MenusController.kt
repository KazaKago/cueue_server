package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.model.MenuRegistrationData
import com.kazakago.cooking_planner.repository.MenuRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MenusController(private val menuRepository: MenuRepository) {

    suspend fun index(call: ApplicationCall) {
        val menus = menuRepository.getMenuRecipesList()
        call.respond(HttpStatusCode.OK, menus)
    }

    suspend fun create(call: ApplicationCall, menu: MenuRegistrationData) {
        menuRepository.createMenu(menu)
        call.respond(HttpStatusCode.Created)
    }
}
