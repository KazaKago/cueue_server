package com.kazakago.cueue.route

import com.kazakago.cueue.config.koin.inject
import com.kazakago.cueue.config.maintenance.maintenanceCheck
import com.kazakago.cueue.config.version.versionCheck
import com.kazakago.cueue.controller.*
import com.kazakago.cueue.model.InvitationCode
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.TagId
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
    val userController by inject<UserController>()
    val recipesController by inject<RecipesController>()
    val recipeController by inject<RecipeController>()
    val tagsController by inject<TagsController>()
    val tagController by inject<TagController>()
    val menusController by inject<MenusController>()
    val menuController by inject<MenuController>()
    val workspacesController by inject<WorkspacesController>()
    val workspaceController by inject<WorkspaceController>()
    val invitationsController by inject<InvitationsController>()
    val invitationController by inject<InvitationController>()
    val invitationAcceptController by inject<InvitationAcceptController>()
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
                    route("/contents") {
                        post {
                            contentsController.create(call, call.requireReceive())
                        }
                    }
                    route("/users") {
                        post {
                            usersController.create(call, call.requirePrincipal(), call.requireReceive())
                        }
                        route("/me") {
                            get {
                                userController.index(call, call.requirePrincipal())
                            }
                            put {
                                userController.update(call, call.requirePrincipal(), call.requireReceive())
                            }
                            route("/workspace") {
                                post {
                                    workspacesController.create(call, call.requirePrincipal(), call.requireReceive())
                                }
                                put {
                                    workspaceController.update(call, call.requirePrincipal(), call.requireReceive())
                                }
                                delete {
                                    workspaceController.delete(call, call.requirePrincipal())
                                }
                            }
                        }
                    }
                    route("/recipes") {
                        get {
                            recipesController.index(call, call.requirePrincipal(), call.request.queryParameters.recipeAfterId(), call.request.queryParameters.keyword(), call.request.queryParameters.tagIds())
                        }
                        post {
                            recipesController.create(call, call.requirePrincipal(), call.requireReceive())
                        }
                        route("/{$RECIPE_ID}") {
                            get {
                                recipeController.index(call, call.requirePrincipal(), call.parameters.recipeId())
                            }
                            put {
                                recipeController.update(call, call.requirePrincipal(), call.parameters.recipeId(), call.requireReceive())
                            }
                            delete {
                                recipeController.delete(call, call.requirePrincipal(), call.parameters.recipeId())
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
                        route("/order") {
                            patch {
                                tagsController.order(call, call.requirePrincipal(), call.requireReceive())
                            }
                        }
                        route("/{$TAG_ID}") {
                            get {
                                tagController.index(call, call.requirePrincipal(), call.parameters.tagId())
                            }
                            put {
                                tagController.update(call, call.requirePrincipal(), call.parameters.tagId(), call.requireReceive())
                            }
                            delete {
                                tagController.delete(call, call.requirePrincipal(), call.parameters.tagId())
                            }
                        }
                    }
                    route("/menus") {
                        get {
                            menusController.index(call, call.requirePrincipal(), call.request.queryParameters.menuAfterId())
                        }
                        post {
                            menusController.create(call, call.requirePrincipal(), call.requireReceive())
                        }
                        route("/{$MENU_ID}") {
                            get {
                                menuController.index(call, call.requirePrincipal(), call.parameters.menuId())
                            }
                            put {
                                menuController.update(call, call.requirePrincipal(), call.parameters.menuId(), call.requireReceive())
                            }
                            delete {
                                menuController.delete(call, call.requirePrincipal(), call.parameters.menuId())
                            }
                        }
                    }
                    route("/invitations") {
                        post {
                            invitationsController.create(call, call.requirePrincipal())
                        }
                        route("/{$INVITATION_CODE}") {
                            get {
                                invitationController.index(call, call.parameters.invitationCode())
                            }
                            route("/accept") {
                                patch {
                                    invitationAcceptController.accept(call, call.requirePrincipal(), call.parameters.invitationCode())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val MENU_ID = "menu_id"
private const val TAG_ID = "tag_id"
private const val RECIPE_ID = "recipe_id"
private const val AFTER_ID = "after_id"
private const val KEYWORD = "keyword"
private const val INVITATION_CODE = "invitation_code"

private fun Parameters.menuId() = requireLong(MENU_ID) { MenuId(it) }
private fun Parameters.tagId() = requireLong(TAG_ID) { TagId(it) }
private fun Parameters.recipeId() = requireLong(RECIPE_ID) { RecipeId(it) }
private fun Parameters.menuAfterId() = getLong(AFTER_ID) { MenuId(it) }
private fun Parameters.recipeAfterId() = getLong(AFTER_ID) { RecipeId(it) }
private fun Parameters.tagIds() = getLongAll(TAG_ID) { TagId(it) }
private fun Parameters.keyword() = getString(KEYWORD)
private fun Parameters.invitationCode() = requireString(INVITATION_CODE) { InvitationCode(it) }
