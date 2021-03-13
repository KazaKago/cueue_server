package com.kazakago.cueue.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.kazakago.cueue.model.AndroidPackage
import com.kazakago.cueue.storage.StorageBucket
import io.ktor.application.*
import io.ktor.util.*
import java.io.File
import java.io.FileInputStream

class Firebase {

    companion object Feature : ApplicationFeature<Application, Unit, Unit> {
        override val key: AttributeKey<Unit> = AttributeKey("Firebase")

        override fun install(pipeline: Application, configure: Unit.() -> Unit) {
            AndroidPackage.applicationId = pipeline.environment.config.property("app.firebase.android_application_id").getString()
            StorageBucket.bucketName = pipeline.environment.config.property("app.firebase.storage_bucket_name").getString()
            val file = File(pipeline.environment.config.property("app.firebase.credentials").getString())
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
    }
}
