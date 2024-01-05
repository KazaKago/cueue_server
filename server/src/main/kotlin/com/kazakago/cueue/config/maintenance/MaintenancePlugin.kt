package com.kazakago.cueue.config.maintenance

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.install
import io.ktor.server.plugins.origin
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext

val Maintenance = createRouteScopedPlugin("Maintenance") {
    val environment = environment ?: throw IllegalStateException()
    val isMaintenanceMode = environment.config.property("app.maintenance.active").getString().toBoolean()
    val excludedIps = environment.config.property("app.maintenance.excluded_ips").getString().filterNot { it.isWhitespace() }.split(",")

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
