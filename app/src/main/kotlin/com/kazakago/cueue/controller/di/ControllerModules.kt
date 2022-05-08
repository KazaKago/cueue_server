package com.kazakago.cueue.controller.di

import com.kazakago.cueue.controller.*
import org.koin.dsl.module

val controllerModules = module {
    single { RootController() }
    single { ContentsController(get()) }
    single { CallbackController() }
    single { UserController(get()) }
    single { WorkspacesController(get(), get()) }
    single { WorkspaceController(get(), get()) }
    single { RecipesController(get(), get()) }
    single { RecipeController(get(), get()) }
    single { TagsController(get(), get()) }
    single { TagController(get(), get()) }
    single { MenusController(get(), get()) }
    single { MenuController(get(), get()) }
}
