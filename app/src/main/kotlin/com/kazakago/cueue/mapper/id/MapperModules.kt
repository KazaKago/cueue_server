package com.kazakago.cueue.mapper.id

import com.kazakago.cueue.mapper.*
import org.koin.dsl.module

val mapperModules = module {
    single { RecipeMapper(get()) }
    single { TagMapper() }
    single { MenuMapper(get(), get()) }
    single { TimeFrameMapper() }
    single { RecipeSummaryMapper() }
}
