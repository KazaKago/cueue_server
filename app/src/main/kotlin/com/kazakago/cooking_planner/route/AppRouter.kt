package com.kazakago.cooking_planner.route

import com.kazakago.cooking_planner.controller.RootController
import io.ktor.application.*
import io.ktor.routing.*

fun Application.appRouting() {
    val rootController = RootController()
//    val cookPlanController = CookPlanController(CookPlanRepository())
    routing {
        route("/") {
            get { rootController.index(call) }
        }
//        route("/cook_plan") {
//            get { cookPlanController.index(call) }
//            post { cookPlanController.create(call, call.receive()) }
//            delete { cookPlanController.delete(call) }
//        }
    }
}