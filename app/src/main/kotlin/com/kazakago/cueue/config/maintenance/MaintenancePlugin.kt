package com.kazakago.cueue.config.maintenance

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val Maintenance = createRouteScopedPlugin("Maintenance") {
    val environment = environment ?: throw IllegalStateException()
    val isMaintenanceMode = environment.config.property("app.maintenance.active").getString().toBoolean()
    val excludedIps = environment.config.property("app.maintenance.excluded_ips").getList()

    onCall { call ->
        if (isMaintenanceMode && !excludedIps.contains(call.request.origin.remoteHost)) {
            call.respond(HttpStatusCode.ServiceUnavailable, "In maintenance.")
        }
    }
}

class MaintenanceRouteSelector : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation.Transparent
    }
}

fun Route.maintenanceCheck(build: Route.() -> Unit): Route {
    return createChild(MaintenanceRouteSelector()).apply {
        install(Maintenance)
        build()
    }
}
