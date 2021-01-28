package com.kazakago.cueue.route

import com.kazakago.cueue.controller.*
import com.kazakago.cueue.mapper.*
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.TagName
import com.kazakago.cueue.repository.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*

fun Application.appRouting() {
    val rootController = RootController()
    val usersController = UsersController(UserRepository(), WorkspaceRepository())
    val recipesController = RecipesController(UserRepository(), RecipeRepository(), RecipeMapper(TagMapper()))
    val recipeController = RecipeController(UserRepository(), RecipeRepository(), RecipeMapper(TagMapper()))
    val tagsController = TagsController(UserRepository(), TagRepository(), TagMapper())
    val tagController = TagController(UserRepository(), TagRepository(), TagMapper())
    val menusController = MenusController(UserRepository(), MenuRepository(), MenuMapper(TimeFrameMapper(), RecipeSummaryMapper()))
    val menuController = MenuController(UserRepository(), MenuRepository(), MenuMapper(TimeFrameMapper(), RecipeSummaryMapper()))
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
                        val afterId = call.request.queryParameters.getLongOrNull("after_id") { RecipeId(it) }
                        val tagName = call.request.queryParameters.getStringOrNull("tag_name") { TagName(it) }
                        recipesController.index(call, call.requirePrincipal(), afterId, tagName)
                    }
                    post {
                        recipesController.create(call, call.requirePrincipal(), call.receiveOrThrow())
                    }
                    route("/{id}") {
                        get {
                            val recipeId = call.parameters.getLong("id") { RecipeId(it) }
                            recipeController.index(call, call.requirePrincipal(), recipeId)
                        }
                        patch {
                            val recipeId = call.parameters.getLong("id") { RecipeId(it) }
                            recipeController.update(call, call.requirePrincipal(), recipeId, call.receiveOrThrow())
                        }
                        delete {
                            val recipeId = call.parameters.getLong("id") { RecipeId(it) }
                            recipeController.delete(call, call.requirePrincipal(), recipeId)
                        }
                    }
                }
                route("/tags") {
                    get {
                        tagsController.index(call, call.requirePrincipal())
                    }
                    post {
                        tagsController.create(call, call.requirePrincipal(), call.receiveOrThrow())
                    }
                    route("/{name}") {
                        get {
                            val tagName = call.parameters.getString("name") { TagName(it) }
                            tagController.index(call, call.requirePrincipal(), tagName)
                        }
                        patch {
                            val tagName = call.parameters.getString("name") { TagName(it) }
                            tagController.update(call, call.requirePrincipal(), tagName, call.receiveOrThrow())
                        }
                        delete {
                            val tagName = call.parameters.getString("name") { TagName(it) }
                            tagController.delete(call, call.requirePrincipal(), tagName)
                        }
                    }
                }
                route("/menus") {
                    get {
                        val afterId = call.request.queryParameters.getLongOrNull("after_id") { MenuId(it) }
                        menusController.index(call, call.requirePrincipal(), afterId)
                    }
                    post {
                        menusController.create(call, call.requirePrincipal(), call.receiveOrThrow())
                    }
                    route("/{id}") {
                        get {
                            val menuId = call.parameters.getLong("id") { MenuId(it) }
                            menuController.index(call, call.requirePrincipal(), menuId)
                        }
                        patch {
                            val menuId = call.parameters.getLong("id") { MenuId(it) }
                            menuController.update(call, call.requirePrincipal(), menuId, call.receiveOrThrow())
                        }
                        delete {
                            val menuId = call.parameters.getLong("id") { MenuId(it) }
                            menuController.delete(call, call.requirePrincipal(), menuId)
                        }
                    }
                }
            }
        }
    }
}
