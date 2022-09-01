package com.kazakago.cueue.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kazakago.cueue.model.AndroidPackage
import com.kazakago.cueue.storage.StorageBucket
import io.ktor.server.application.*
import java.io.File

val Firebase = createApplicationPlugin(name = "Firebase") {
    val environment = environment ?: throw IllegalStateException()
    AndroidPackage.applicationId = environment.config.property("app.firebase.android_application_id").getString()
    StorageBucket.bucketName = environment.config.property("app.firebase.storage_bucket_name").getString()
    val credentials = environment.config.property("app.firebase.credentials").getString()
    val file = File(credentials)
    val stream = if (file.exists()) {
        file.inputStream()
    } else {
        credentials.byteInputStream()
    }
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(stream))
        .setStorageBucket(StorageBucket.bucketName)
        .build()
    FirebaseApp.initializeApp(options)
}
