package com.kazakago.cueue.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.*
import java.io.FileInputStream

fun Application.initializeFirebase() {
    val serviceAccount = FileInputStream("app/cueue-8ad42-firebase-adminsdk-8jekl-940ada59c9.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
}
