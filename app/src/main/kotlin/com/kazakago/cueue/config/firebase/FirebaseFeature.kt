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
            val file = File(pipeline.environment.config.property("app.firebase.credentials").getString())
            if (file.exists()) {
                val serviceAccount = FileInputStream(file)
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
                FirebaseApp.initializeApp(options)
            } else {
                FirebaseApp.initializeApp()
            }
        }
    }
}
