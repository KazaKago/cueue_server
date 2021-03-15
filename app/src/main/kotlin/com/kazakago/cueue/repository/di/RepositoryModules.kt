package com.kazakago.cueue.repository.di

import com.kazakago.cueue.repository.*
import org.koin.dsl.module

val repositoryModules = module {
    single { ContentRepository(get()) }
    single { UserRepository(get()) }
    single { WorkspaceRepository(get()) }
    single { RecipeRepository(get()) }
    single { TagRepository(get()) }
    single { MenuRepository(get()) }
}
