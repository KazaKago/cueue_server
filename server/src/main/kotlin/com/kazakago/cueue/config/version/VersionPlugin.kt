package com.kazakago.cueue.config.version

import com.kazakago.cueue.model.MinApiVersion
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.install
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext

val Version = createRouteScopedPlugin("Version") {
    onCall { call ->
        val apiVersion = call.request.headers["Api-Version"]?.toIntOrNull()
        if (apiVersion != null) {
            try {
                if (apiVersion < MinApiVersion.value) {
                    call.respond(HttpStatusCode.UpgradeRequired, "Require Api-Version '${MinApiVersion.value}' or higher.")
                }
            } catch (exception: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, exception.localizedMessage)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, "Require 'Api-Version' header.")
        }
    }
}

class VersionRouteSelector : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation.Transparent
    }
}

fun Route.versionCheck(build: Route.() -> Unit): Route {
    return createChild(VersionRouteSelector()).apply {
        install(Version)
        build()
    }
}
