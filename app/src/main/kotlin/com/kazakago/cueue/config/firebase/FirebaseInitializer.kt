package com.kazakago.cueue.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.*
import java.io.FileInputStream

fun Application.initializeFirebase() {
    val serviceAccount = FileInputStream(environment.config.property("app.google.credentials").getString())
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
}
