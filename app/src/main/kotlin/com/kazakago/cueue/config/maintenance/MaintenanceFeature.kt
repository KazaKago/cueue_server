package com.kazakago.cueue.config.maintenance

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*

class Maintenance(private val isMaintenanceMode: Boolean, private val excludedIps: List<String>) {

    companion object Feature : ApplicationFeature<Application, Unit, Maintenance> {
        override val key: AttributeKey<Maintenance> = AttributeKey("Maintenance")

        override fun install(pipeline: Application, configure: Unit.() -> Unit): Maintenance {
            val isMaintenanceMode = pipeline.environment.config.property("app.maintenance.active").getString().toBoolean()
            val excludedIps = pipeline.environment.config.property("app.maintenance.excluded_ips").getList()
            return Maintenance(isMaintenanceMode, excludedIps)
        }
    }

    fun interceptPipeline(pipeline: ApplicationCallPipeline) {
        pipeline.intercept(ApplicationCallPipeline.Setup) {
            if (isMaintenanceMode && !excludedIps.contains(call.request.origin.remoteHost)) {
                call.respond(HttpStatusCode.ServiceUnavailable, "In maintenance.")
                finish()
            }
        }
    }
}

class MaintenanceRouteSelector : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation(true, RouteSelectorEvaluation.qualityTransparent)
    }
}

fun Route.maintenanceCheck(build: Route.() -> Unit): Route {
    return createChild(MaintenanceRouteSelector()).apply {
        application.feature(Maintenance).interceptPipeline(this)
        build()
    }
}
