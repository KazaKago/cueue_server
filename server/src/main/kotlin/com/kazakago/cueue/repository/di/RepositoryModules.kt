package com.kazakago.cueue.repository.di

import com.kazakago.cueue.repository.ContentRepository
import com.kazakago.cueue.repository.InvitationRepository
import com.kazakago.cueue.repository.MenuRepository
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.TagRepository
import com.kazakago.cueue.repository.UserRepository
import org.koin.dsl.module

val repositoryModules = module {
    single { ContentRepository(get()) }
    single { UserRepository(get()) }
    single { RecipeRepository(get(), get()) }
    single { TagRepository(get()) }
    single { MenuRepository(get(), get()) }
    single { InvitationRepository(get()) }
}
