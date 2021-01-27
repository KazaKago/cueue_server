package com.kazakago.cueue.config.firebase

import com.kazakago.cueue.auth.firebase.FirebaseAuthenticationProvider
import com.kazakago.cueue.model.UID

object AuthConfig {
    fun FirebaseAuthenticationProvider.Configuration.configure() {
        principal = { uid ->
            UID(uid)
        }
    }
}
