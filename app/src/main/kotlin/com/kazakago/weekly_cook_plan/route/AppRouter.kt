package com.kazakago.weekly_cook_plan.route

import com.kazakago.weekly_cook_plan.controller.CookPlanController
import com.kazakago.weekly_cook_plan.controller.RootController
import com.kazakago.weekly_cook_plan.repository.CookPlanRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.appRouting() {
    val rootController = RootController()
    val cookPlanController = CookPlanController(CookPlanRepository())
    routing {
        route("/") {
            get { rootController.index(call) }
        }
        route("/cook_plan") {
            get { cookPlanController.index(call) }
            post { cookPlanController.create(call, call.receive()) }
            delete { cookPlanController.delete(call) }
        }
    }
}