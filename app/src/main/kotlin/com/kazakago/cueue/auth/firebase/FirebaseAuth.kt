package com.kazakago.cueue.auth.firebase

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

class FirebaseAuthenticationProvider internal constructor(config: Configuration) : AuthenticationProvider(config) {

    internal val token: (ApplicationCall) -> String? = config.token
    internal val principle: ((verifiedToken: FirebaseToken) -> Principal?)? = config.principal

    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name) {

        internal var token: (ApplicationCall) -> String? = { call -> call.request.parseAuthorizationToken() }

        internal var principal: ((verifiedToken: FirebaseToken) -> Principal?)? = null

        internal fun build() = FirebaseAuthenticationProvider(this)
    }
}

fun Authentication.Configuration.firebase(name: String? = null, configure: FirebaseAuthenticationProvider.Configuration.() -> Unit) {
    val provider = FirebaseAuthenticationProvider.Configuration(name).apply(configure).build()
    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        try {
            val token = provider.token(call) ?: throw FirebaseAuthException(FirebaseException(ErrorCode.UNAUTHENTICATED, "No token could be found", null))
            val verifiedToken = FirebaseAuth.getInstance().verifyIdToken(token)
            provider.principle?.let { it.invoke(verifiedToken)?.let { principle -> context.principal(principle) } }
        } catch (cause: Throwable) {
            val message = if (cause is FirebaseAuthException) {
                "Authentication failed: ${cause.message ?: cause.javaClass.simpleName}"
            } else {
                cause.message ?: cause.javaClass.simpleName
            }
            call.respond(HttpStatusCode.Unauthorized, message)
            context.challenge.complete()
            finish()
        }
    }
    register(provider)
}

private fun ApplicationRequest.parseAuthorizationToken(): String? = authorization()?.let {
    it.split(" ")[1]
}
