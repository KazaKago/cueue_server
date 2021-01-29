package com.kazakago.cueue.repository.di

import com.kazakago.cueue.repository.*
import org.koin.dsl.module

val repositoryModules = module {
    single { UserRepository() }
    single { WorkspaceRepository() }
    single { RecipeRepository() }
    single { TagRepository() }
    single { MenuRepository() }
}
