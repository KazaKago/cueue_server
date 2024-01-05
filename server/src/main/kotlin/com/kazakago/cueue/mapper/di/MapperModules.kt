package com.kazakago.cueue.mapper.di

import com.kazakago.cueue.mapper.InvitationMapper
import com.kazakago.cueue.mapper.MenuMapper
import com.kazakago.cueue.mapper.MenuSummaryMapper
import com.kazakago.cueue.mapper.RecipeMapper
import com.kazakago.cueue.mapper.RecipeSummaryMapper
import com.kazakago.cueue.mapper.TagMapper
import com.kazakago.cueue.mapper.TimeFrameMapper
import com.kazakago.cueue.mapper.UserMapper
import com.kazakago.cueue.mapper.UserSummaryMapper
import com.kazakago.cueue.mapper.WorkspaceMapper
import org.koin.dsl.module

val mapperModules = module {
    single { UserMapper(get()) }
    single { UserSummaryMapper() }
    single { WorkspaceMapper(get()) }
    single { RecipeMapper(get()) }
    single { RecipeSummaryMapper() }
    single { TagMapper() }
    single { MenuMapper(get(), get()) }
    single { MenuSummaryMapper(get(), get()) }
    single { TimeFrameMapper() }
    single { InvitationMapper(get()) }
}
