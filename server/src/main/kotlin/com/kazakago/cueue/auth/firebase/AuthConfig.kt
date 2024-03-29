package com.kazakago.cueue.auth.firebase

import com.kazakago.cueue.model.Email
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.UID
import java.net.URL

object AuthConfig {
    fun FirebaseAuthenticationProvider.Config.configure() {
        principal = { verifiedToken ->
            FirebaseUser(
                uid = UID(verifiedToken.uid),
                name = verifiedToken.name,
                picture = verifiedToken.picture?.let { URL(it) },
                email = Email(verifiedToken.email),
                isEmailVerified = verifiedToken.isEmailVerified
            )
        }
    }
}
