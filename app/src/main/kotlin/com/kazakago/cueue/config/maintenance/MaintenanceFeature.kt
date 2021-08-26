package com.kazakago.cueue.config.maintenance

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*

class Maintenance(private val isMaintenanceMode: Boolean) {

    companion object Feature : ApplicationFeature<Application, Unit, Maintenance> {
        override val key: AttributeKey<Maintenance> = AttributeKey("Maintenance")

        override fun install(pipeline: Application, configure: Unit.() -> Unit): Maintenance {
            val isMaintenanceMode = pipeline.environment.config.property("app.maintenance").getString().toBoolean()
            return Maintenance(isMaintenanceMode)
        }
    }

    fun interceptPipeline(pipeline: ApplicationCallPipeline) {
        pipeline.intercept(ApplicationCallPipeline.Features) {
            if (isMaintenanceMode) {
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
