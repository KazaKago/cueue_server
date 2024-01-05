package com.kazakago.cueue.model

import io.ktor.server.auth.Principal
import java.net.URL

data class FirebaseUser(
    val uid: UID,
    val name: String?,
    val picture: URL?,
    val email: Email,
    val isEmailVerified: Boolean,
) : Principal
