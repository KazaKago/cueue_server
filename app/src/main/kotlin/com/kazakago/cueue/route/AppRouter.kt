package com.kazakago.cueue.route

import com.kazakago.cueue.config.maintenance.maintenanceCheck
import com.kazakago.cueue.config.version.versionCheck
import com.kazakago.cueue.controller.*
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.WorkspaceId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.appRouting() {
    val rootController by inject<RootController>()
    val contentsController by inject<ContentsController>()
    val callbackController by inject<CallbackController>()
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
            post("/callback/sign_in_with_apple") {
                callbackController.create(call, call.receiveText())
            }
        }
        maintenanceCheck {
            versionCheck {
                authenticate {
                    route("/users") {
                        get {
                            usersController.index(call, call.requirePrincipal())
                        }
                        post {
                            usersController.create(call, call.requirePrincipal())
                        }
                    }
                    route("/contents") {
                        post {
                            contentsController.create(call, call.requireReceive())
                        }
                    }
                    route("/{workspace_id}") {
                        route("/recipes") {
                            get {
                                val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                val afterId = call.request.queryParameters.getLong("after_id") { RecipeId(it) }
                                val tagId = call.request.queryParameters.getLong("tag_id") { TagId(it) }
                                recipesController.index(call, call.requirePrincipal(), workspaceId, afterId, tagId)
                            }
                            post {
                                val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                recipesController.create(call, call.requirePrincipal(), workspaceId, call.requireReceive())
                            }
                            route("/{recipe_id}") {
                                get {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val recipeId = call.parameters.requireLong("recipe_id") { RecipeId(it) }
                                    recipeController.index(call, call.requirePrincipal(), workspaceId, recipeId)
                                }
                                patch {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val recipeId = call.parameters.requireLong("recipe_id") { RecipeId(it) }
                                    recipeController.update(call, call.requirePrincipal(), workspaceId, recipeId, call.requireReceive())
                                }
                                delete {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val recipeId = call.parameters.requireLong("recipe_id") { RecipeId(it) }
                                    recipeController.delete(call, call.requirePrincipal(), workspaceId, recipeId)
                                }
                            }
                        }
                        route("/tags") {
                            get {
                                val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                tagsController.index(call, call.requirePrincipal(), workspaceId)
                            }
                            post {
                                val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                tagsController.create(call, call.requirePrincipal(), workspaceId, call.requireReceive())
                            }
                            route("/{tag_id}") {
                                get {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val tagId = call.parameters.requireLong("tag_id") { TagId(it) }
                                    tagController.index(call, call.requirePrincipal(), workspaceId, tagId)
                                }
                                patch {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val tagId = call.parameters.requireLong("tag_id") { TagId(it) }
                                    tagController.update(call, call.requirePrincipal(), workspaceId, tagId, call.requireReceive())
                                }
                                delete {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val tagId = call.parameters.requireLong("tag_id") { TagId(it) }
                                    tagController.delete(call, call.requirePrincipal(), workspaceId, tagId)
                                }
                            }
                        }
                        route("/menus") {
                            get {
                                val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                val afterId = call.request.queryParameters.getLong("after_id") { MenuId(it) }
                                menusController.index(call, call.requirePrincipal(), workspaceId, afterId)
                            }
                            post {
                                val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                menusController.create(call, call.requirePrincipal(), workspaceId, call.requireReceive())
                            }
                            route("/{menu_id}") {
                                get {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val menuId = call.parameters.requireLong("menu_id") { MenuId(it) }
                                    menuController.index(call, call.requirePrincipal(), workspaceId, menuId)
                                }
                                patch {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val menuId = call.parameters.requireLong("menu_id") { MenuId(it) }
                                    menuController.update(call, call.requirePrincipal(), workspaceId, menuId, call.requireReceive())
                                }
                                delete {
                                    val workspaceId = call.parameters.requireLong("workspace_id") { WorkspaceId(it) }
                                    val menuId = call.parameters.requireLong("menu_id") { MenuId(it) }
                                    menuController.delete(call, call.requirePrincipal(), workspaceId, menuId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
