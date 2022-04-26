package com.kazakago.cueue.route

import com.kazakago.cueue.config.koin.inject
import com.kazakago.cueue.config.maintenance.maintenanceCheck
import com.kazakago.cueue.config.version.versionCheck
import com.kazakago.cueue.controller.*
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.model.UnsafeWorkspaceId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

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
                    route("/{$WORKSPACE_ID}") {
                        route("/recipes") {
                            get {
                                recipesController.index(call, call.requirePrincipal(), call.parameters.workspaceId(), call.request.queryParameters.recipeAfterId(), call.request.queryParameters.tagIdOrNull(), call.request.queryParameters.title())
                            }
                            post {
                                recipesController.create(call, call.requirePrincipal(), call.parameters.workspaceId(), call.requireReceive())
                            }
                            route("/{$RECIPE_ID}") {
                                get {
                                    recipeController.index(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.recipeId())
                                }
                                patch {
                                    recipeController.update(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.recipeId(), call.requireReceive())
                                }
                                delete {
                                    recipeController.delete(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.recipeId())
                                }
                            }
                        }
                        route("/tags") {
                            get {
                                tagsController.index(call, call.requirePrincipal(), call.parameters.workspaceId())
                            }
                            post {
                                tagsController.create(call, call.requirePrincipal(), call.parameters.workspaceId(), call.requireReceive())
                            }
                            route("/order") {
                                patch {
                                    tagsController.order(call, call.requirePrincipal(), call.parameters.workspaceId(), call.requireReceive())
                                }
                            }
                            route("/{$TAG_ID}") {
                                get {
                                    tagController.index(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.tagId())
                                }
                                patch {
                                    tagController.update(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.tagId(), call.requireReceive())
                                }
                                delete {
                                    tagController.delete(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.tagId())
                                }
                            }
                        }
                        route("/menus") {
                            get {
                                menusController.index(call, call.requirePrincipal(), call.parameters.workspaceId(), call.request.queryParameters.menuAfterId())
                            }
                            post {
                                menusController.create(call, call.requirePrincipal(), call.parameters.workspaceId(), call.requireReceive())
                            }
                            route("/{$MENU_ID}") {
                                get {
                                    menuController.index(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.menuId())
                                }
                                patch {
                                    menuController.update(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.menuId(), call.requireReceive())
                                }
                                delete {
                                    menuController.delete(call, call.requirePrincipal(), call.parameters.workspaceId(), call.parameters.menuId())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val WORKSPACE_ID = "workspace_id"
private const val MENU_ID = "menu_id"
private const val TAG_ID = "tag_id"
private const val RECIPE_ID = "recipe_id"
private const val AFTER_ID = "after_id"
private const val TITLE = "title"

private fun Parameters.workspaceId() = requireLong(WORKSPACE_ID) { UnsafeWorkspaceId(it) }
private fun Parameters.menuId() = requireLong(MENU_ID) { MenuId(it) }
private fun Parameters.tagId() = requireLong(TAG_ID) { TagId(it) }
private fun Parameters.recipeId() = requireLong(RECIPE_ID) { RecipeId(it) }
private fun Parameters.menuAfterId() = getLong(AFTER_ID) { MenuId(it) }
private fun Parameters.recipeAfterId() = getLong(AFTER_ID) { RecipeId(it) }
private fun Parameters.tagIdOrNull() = getLong(TAG_ID) { TagId(it) }
private fun Parameters.title() = getString(TITLE)
