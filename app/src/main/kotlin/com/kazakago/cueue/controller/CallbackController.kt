package com.kazakago.cueue.controller

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class CallbackController {

    suspend fun create(call: ApplicationCall, token: String) {
        val applicationId = "com.kazakago.cueue"
        val redirect = "intent://callback?$token#Intent;package=$applicationId;scheme=signinwithapple;end"
        call.respond(HttpStatusCode.TemporaryRedirect, redirect)
    }
}
