package com.kazakago.cueue.storage.di

import com.google.firebase.cloud.StorageClient
import org.koin.dsl.module

val storageModules = module {
    single { StorageClient.getInstance().bucket() }
}
