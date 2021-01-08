package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.model.WeeklyCookPlan
import com.kazakago.cooking_planner.repository.CookPlanRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class CookPlanController(private val cookPlanRepository: CookPlanRepository) {

    suspend fun index(call: ApplicationCall) {
        val weeklyCookPlan = cookPlanRepository.getWeeklyCookPlan()
        call.respond(HttpStatusCode.OK, weeklyCookPlan)
    }

    suspend fun create(call: ApplicationCall, weeklyCookPlan: WeeklyCookPlan) {
        cookPlanRepository.setWeeklyCookPlan(weeklyCookPlan)
        call.respond(HttpStatusCode.Created)
    }

    suspend fun delete(call: ApplicationCall) {
        cookPlanRepository.deleteWeeklyCookPlan()
        call.respond(HttpStatusCode.NoContent)
    }
}
