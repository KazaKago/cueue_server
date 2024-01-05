package com.kazakago.cueue.controller

import com.kazakago.cueue.model.AndroidPackage
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondRedirect

class CallbackController {

    suspend fun create(call: ApplicationCall, token: String) {
        val redirect = "intent://callback?$token#Intent;package=${AndroidPackage.applicationId};scheme=signinwithapple;end"
        call.respondRedirect(redirect)
    }
}
