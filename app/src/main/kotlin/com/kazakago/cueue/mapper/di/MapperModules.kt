package com.kazakago.cueue.mapper.di

import com.kazakago.cueue.mapper.*
import org.koin.dsl.module

val mapperModules = module {
    single { UserMapper(get()) }
    single { WorkspaceMapper() }
    single { RecipeMapper(get()) }
    single { RecipeSummaryMapper() }
    single { TagMapper() }
    single { MenuMapper(get(), get()) }
    single { MenuSummaryMapper(get(), get()) }
    single { TimeFrameMapper() }
}
