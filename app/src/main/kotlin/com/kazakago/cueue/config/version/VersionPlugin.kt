package com.kazakago.cueue.config.version

import com.kazakago.cueue.model.MinApiVersion
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.swiftzer.semver.SemVer

val Version = createRouteScopedPlugin("Version") {
    onCall { call ->
        val rawApiVersion = call.request.headers["Api-Version"]
        if (rawApiVersion != null) {
            try {
                val apiVersion = SemVer.parse(rawApiVersion)
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
