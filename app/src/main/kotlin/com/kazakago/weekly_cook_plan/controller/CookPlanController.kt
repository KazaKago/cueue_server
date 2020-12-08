package com.kazakago.weekly_cook_plan.controller

import com.kazakago.weekly_cook_plan.model.CookPlan
import com.kazakago.weekly_cook_plan.model.WeeklyCookPlan
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class CookPlanController {

    suspend fun index(call: ApplicationCall) {
        val weeklyCookPlan = WeeklyCookPlan(
            sunday = CookPlan("焼き肉"),
            monday = CookPlan("ラーメン"),
            tuesday = CookPlan("お寿司"),
            wednesday = CookPlan("中華"),
            thursday = CookPlan("フレンチ"),
            friday = CookPlan("ドリア"),
            saturday = CookPlan("チキン"),
        )
        call.respond(HttpStatusCode.OK, weeklyCookPlan)
    }

    suspend fun create(call: ApplicationCall, weeklyCookPlan: WeeklyCookPlan) {
        call.respond(HttpStatusCode.Created)
    }

    suspend fun delete(call: ApplicationCall) {
        call.respond(HttpStatusCode.NoContent)
    }
}
