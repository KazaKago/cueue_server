package com.kazakago.cueue.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.*
import io.ktor.util.*
import java.io.File
import java.io.FileInputStream

class Firebase {

    companion object Feature : ApplicationFeature<Application, Unit, Unit> {
        override val key: AttributeKey<Unit> = AttributeKey("Firebase")

        override fun install(pipeline: Application, configure: Unit.() -> Unit) {
            val file = File(pipeline.environment.config.property("app.google.credentials").getString())
            val options = if (file.exists()) {
                val serviceAccount = FileInputStream(file)
                FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
            } else {
                FirebaseOptions.builder().build()
            }
            FirebaseApp.initializeApp(options)
        }
    }
}
