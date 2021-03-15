package com.kazakago.cueue.controller.di

import com.kazakago.cueue.controller.*
import org.koin.dsl.module

val controllerModules = module {
    single { RootController() }
    single { ContentsController(get()) }
    single { CallbackController() }
    single { UsersController(get(), get()) }
    single { RecipesController(get(), get(), get()) }
    single { RecipeController(get(), get(), get()) }
    single { TagsController(get(), get(), get()) }
    single { TagController(get(), get(), get()) }
    single { MenusController(get(), get(), get()) }
    single { MenuController(get(), get(), get()) }
}
