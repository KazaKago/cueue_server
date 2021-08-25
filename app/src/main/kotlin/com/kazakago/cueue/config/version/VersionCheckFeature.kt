package com.kazakago.cueue.config.version

import com.kazakago.cueue.model.MinApiVersion
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import net.swiftzer.semver.SemVer

class VersionCheck {

    companion object Feature : ApplicationFeature<Application, Unit, VersionCheck> {
        override val key: AttributeKey<VersionCheck> = AttributeKey("Version")

        override fun install(pipeline: Application, configure: Unit.() -> Unit): VersionCheck {
            return VersionCheck()
        }
    }

    fun interceptPipeline(pipeline: ApplicationCallPipeline) {
        pipeline.intercept(ApplicationCallPipeline.Features) {
            val rawApiVersion = call.request.headers["Api-Version"]
            if (rawApiVersion != null) {
                try {
                    val apiVersion = SemVer.parse(rawApiVersion)
                    if (apiVersion < MinApiVersion.value) {
                        call.respond(HttpStatusCode.UpgradeRequired, "Require Api-Version '${MinApiVersion.value}' or higher.")
                        finish()
                    }
                } catch (exception: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, exception.localizedMessage)
                    finish()
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Require 'Api-Version' header.")
                finish()
            }
        }
    }
}

class VersionCheckRouteSelector : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation(true, RouteSelectorEvaluation.qualityTransparent)
    }
}

fun Route.versionCheck(build: Route.() -> Unit): Route {
    val versionCheckedRoute = createChild(VersionCheckRouteSelector())
    application.feature(VersionCheck).interceptPipeline(versionCheckedRoute)
    versionCheckedRoute.build()
    return versionCheckedRoute
}
