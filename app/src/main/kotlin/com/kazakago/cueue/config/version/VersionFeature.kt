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

    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        configurationNames: List<String?> = listOf(null),
        optional: Boolean = false,
    ) {
        require(configurationNames.isNotEmpty()) { "At least one configuration name or default listOf(null)" }

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

class VersionCheckRouteSelector(private val names: List<String?>) : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation(true, RouteSelectorEvaluation.qualityTransparent)
    }

    override fun toString(): String = "(versionCheck ${names.joinToString { it ?: "\"default\"" }})"
}

fun Route.versionCheck(
    vararg configurations: String? = arrayOf(null),
    optional: Boolean = false,
    build: Route.() -> Unit
): Route {
    require(configurations.isNotEmpty()) { "At least one configuration name or null for default need to be provided" }
    val configurationNames = configurations.distinct()
    val authenticatedRoute = createChild(VersionCheckRouteSelector(configurationNames))

    application.feature(VersionCheck).interceptPipeline(authenticatedRoute, configurationNames, optional = optional)
    authenticatedRoute.build()
    return authenticatedRoute
}
