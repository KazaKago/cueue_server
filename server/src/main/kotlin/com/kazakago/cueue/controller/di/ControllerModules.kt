package com.kazakago.cueue.controller.di

import com.kazakago.cueue.controller.ContentsController
import com.kazakago.cueue.controller.InvitationAcceptController
import com.kazakago.cueue.controller.InvitationController
import com.kazakago.cueue.controller.InvitationsController
import com.kazakago.cueue.controller.MenuController
import com.kazakago.cueue.controller.MenusController
import com.kazakago.cueue.controller.RecipeController
import com.kazakago.cueue.controller.RecipesController
import com.kazakago.cueue.controller.RootController
import com.kazakago.cueue.controller.TagController
import com.kazakago.cueue.controller.TagsController
import com.kazakago.cueue.controller.UserController
import com.kazakago.cueue.controller.UsersController
import org.koin.dsl.module

val controllerModules = module {
    single { RootController() }
    single { ContentsController(get()) }
    single { UsersController(get()) }
    single { UserController(get()) }
    single { RecipesController(get(), get()) }
    single { RecipeController(get(), get()) }
    single { TagsController(get(), get()) }
    single { TagController(get(), get()) }
    single { MenusController(get(), get()) }
    single { MenuController(get(), get()) }
    single { InvitationsController(get(), get()) }
    single { InvitationController(get()) }
    single { InvitationAcceptController(get(), get()) }
}
