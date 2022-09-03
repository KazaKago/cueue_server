package com.kazakago.cueue.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kazakago.cueue.model.AndroidPackage
import com.kazakago.cueue.storage.StorageBucket
import io.ktor.server.application.*

val Firebase = createApplicationPlugin(name = "Firebase") {
    val environment = environment ?: throw IllegalStateException()
    AndroidPackage.applicationId = environment.config.property("app.firebase.android_application_id").getString()
    StorageBucket.bucketName = environment.config.property("app.firebase.storage_bucket_name").getString()
    val credentials = FirebaseCredentials(environment.config.property("app.firebase.credentials").toString())
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(credentials.inputStream))
        .setStorageBucket(StorageBucket.bucketName)
        .build()
    FirebaseApp.initializeApp(options)
}
