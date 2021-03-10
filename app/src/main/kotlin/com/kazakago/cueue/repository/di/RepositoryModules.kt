package com.kazakago.cueue.repository.di

import com.kazakago.cueue.repository.*
import org.koin.dsl.module

val repositoryModules = module {
    single { UserRepository() }
    single { WorkspaceRepository() }
    single { RecipeRepository(get()) }
    single { TagRepository() }
    single { MenuRepository() }
}
