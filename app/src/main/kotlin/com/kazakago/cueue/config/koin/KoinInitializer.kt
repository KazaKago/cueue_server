package com.kazakago.cueue.config.koin

import com.kazakago.cueue.controller.di.controllerModules
import com.kazakago.cueue.mapper.id.mapperModules
import com.kazakago.cueue.repository.di.repositoryModules
import org.koin.core.KoinApplication

fun KoinApplication.register() {
    modules(controllerModules)
    modules(repositoryModules)
    modules(mapperModules)
}
