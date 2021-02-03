package com.kazakago.cueue.route

import com.kazakago.cueue.controller.*
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.TagId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.appRouting() {
    val rootController by inject<RootController>()
    val usersController by inject<UsersController>()
    val recipesController by inject<RecipesController>()
    val recipeController by inject<RecipeController>()
    val tagsController by inject<TagsController>()
    val tagController by inject<TagController>()
    val menusController by inject<MenusController>()
    val menuController by inject<MenuController>()
    routing {
        route("/") {
            get {
                rootController.index(call)
            }
        }
        route("/api") {
            authenticate {
                route("/users") {
                    post {
                        usersController.create(call, call.requirePrincipal())
                    }
                }
                route("/recipes") {
                    get {
                        val afterId = call.request.queryParameters.getLong("after_id") { RecipeId(it) }
                        val tagId = call.request.queryParameters.getLong("tag_id") { TagId(it) }
                        recipesController.index(call, call.requirePrincipal(), afterId, tagId)
                    }
                    post {
                        recipesController.create(call, call.requirePrincipal(), call.requireReceive())
                    }
                    route("/{id}") {
                        get {
                            val recipeId = call.parameters.requireLong("id") { RecipeId(it) }
                            recipeController.index(call, call.requirePrincipal(), recipeId)
                        }
                        patch {
                            val recipeId = call.parameters.requireLong("id") { RecipeId(it) }
                            recipeController.update(call, call.requirePrincipal(), recipeId, call.requireReceive())
                        }
                        delete {
                            val recipeId = call.parameters.requireLong("id") { RecipeId(it) }
                            recipeController.delete(call, call.requirePrincipal(), recipeId)
                        }
                    }
                }
                route("/tags") {
                    get {
                        tagsController.index(call, call.requirePrincipal())
                    }
                    post {
                        tagsController.create(call, call.requirePrincipal(), call.requireReceive())
                    }
                    route("/{id}") {
                        get {
                            val tagId = call.parameters.requireLong("id") { TagId(it) }
                            tagController.index(call, call.requirePrincipal(), tagId)
                        }
                        patch {
                            val tagId = call.parameters.requireLong("id") { TagId(it) }
                            tagController.update(call, call.requirePrincipal(), tagId, call.requireReceive())
                        }
                        delete {
                            val tagId = call.parameters.requireLong("id") { TagId(it) }
                            tagController.delete(call, call.requirePrincipal(), tagId)
                        }
                    }
                }
                route("/menus") {
                    get {
                        val afterId = call.request.queryParameters.getLong("after_id") { MenuId(it) }
                        menusController.index(call, call.requirePrincipal(), afterId)
                    }
                    post {
                        menusController.create(call, call.requirePrincipal(), call.requireReceive())
                    }
                    route("/{id}") {
                        get {
                            val menuId = call.parameters.requireLong("id") { MenuId(it) }
                            menuController.index(call, call.requirePrincipal(), menuId)
                        }
                        patch {
                            val menuId = call.parameters.requireLong("id") { MenuId(it) }
                            menuController.update(call, call.requirePrincipal(), menuId, call.requireReceive())
                        }
                        delete {
                            val menuId = call.parameters.requireLong("id") { MenuId(it) }
                            menuController.delete(call, call.requirePrincipal(), menuId)
                        }
                    }
                }
            }
        }
    }
}
