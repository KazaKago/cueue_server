package com.kazakago.cueue.auth.firebase

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.auth.Principal
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.authorization
import io.ktor.server.response.respond

class FirebaseAuthenticationProvider private constructor(config: Config) : AuthenticationProvider(config) {

    private val token: (ApplicationCall) -> String? = config.token
    private val principle: ((verifiedToken: FirebaseToken) -> Principal?)? = config.principal

    class Config internal constructor(name: String?) : AuthenticationProvider.Config(name) {

        internal var token: (ApplicationCall) -> String? = { call -> call.request.parseAuthorizationToken() }

        internal var principal: ((verifiedToken: FirebaseToken) -> Principal?)? = null

        internal fun build() = FirebaseAuthenticationProvider(this)
    }

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        try {
            val token = token(context.call) ?: throw FirebaseAuthException(FirebaseException(ErrorCode.UNAUTHENTICATED, "No token could be found", null))
            val verifiedToken = FirebaseAuth.getInstance().verifyIdToken(token)
            principle?.let { it.invoke(verifiedToken)?.let { principle -> context.principal(principle) } }
        } catch (cause: Throwable) {
            val message = if (cause is FirebaseAuthException) {
                "Authentication failed: ${cause.message ?: cause.javaClass.simpleName}"
            } else {
                cause.message ?: cause.javaClass.simpleName
            }
            context.call.respond(HttpStatusCode.Unauthorized, message)
            context.challenge.complete()
        }
    }
}

fun AuthenticationConfig.firebase(name: String? = null, configure: FirebaseAuthenticationProvider.Config.() -> Unit) {
    val provider = FirebaseAuthenticationProvider.Config(name).apply(configure).build()
    register(provider)
}

private fun ApplicationRequest.parseAuthorizationToken(): String? = authorization()?.let {
    it.split(" ")[1]
}
