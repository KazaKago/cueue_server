package com.kazakago.cueue.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kazakago.cueue.model.AndroidPackage
import com.kazakago.cueue.storage.StorageBucket
import io.ktor.server.application.*
import java.io.File
import java.io.FileInputStream

val Firebase = createApplicationPlugin(name = "Firebase") {
    val environment = environment ?: throw IllegalStateException()
    AndroidPackage.applicationId = environment.config.property("app.firebase.android_application_id").getString()
    StorageBucket.bucketName = environment.config.property("app.firebase.storage_bucket_name").getString()
    val file = File(environment.config.property("app.firebase.credentials").getString())
    if (file.exists()) {
        FileInputStream(file).use {
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(it))
                .setStorageBucket(StorageBucket.bucketName)
                .build()
            FirebaseApp.initializeApp(options)
        }
    } else {
        FirebaseApp.initializeApp()
    }
}
