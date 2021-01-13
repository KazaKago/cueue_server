package com.kazakago.cooking_planner.route

import com.kazakago.cooking_planner.controller.*
import com.kazakago.cooking_planner.mapper.*
import com.kazakago.cooking_planner.model.MenuId
import com.kazakago.cooking_planner.model.RecipeId
import com.kazakago.cooking_planner.model.TagName
import com.kazakago.cooking_planner.model.UserId
import com.kazakago.cooking_planner.repository.MenuRepository
import com.kazakago.cooking_planner.repository.RecipeRepository
import com.kazakago.cooking_planner.repository.TagRepository
import com.kazakago.cooking_planner.repository.UserRepository
import io.ktor.application.*
import io.ktor.routing.*

fun Application.appRouting() {
    val rootController = RootController()
    val usersController = UsersController(UserRepository(UserMapper()))
    val userController = UserController(UserRepository(UserMapper()))
    val recipesController = RecipesController(RecipeRepository(RecipeMapper(TagMapper())))
    val recipeController = RecipeController(RecipeRepository(RecipeMapper(TagMapper())))
    val tagsController = TagsController(TagRepository(TagMapper()))
    val tagController = TagController(TagRepository(TagMapper()))
    val menusController = MenusController(MenuRepository(MenuMapper(TimeFrameMapper(), RecipeSummaryMapper())))
    val menuController = MenuController(MenuRepository(MenuMapper(TimeFrameMapper(), RecipeSummaryMapper())))
    routing {
        route("/") {
            get {
                rootController.index(call)
            }
        }
        route("/users") {
            get {
                usersController.index(call)
            }
            post {
                usersController.create(call, call.receiveOrThrow())
            }
        }
        route("/users/{id}") {
            get {
                val userId = call.parameters.getLong("id") { UserId(it) }
                userController.index(call, userId)
            }
            patch {
                val userId = call.parameters.getLong("id") { UserId(it) }
                userController.update(call, userId, call.receiveOrThrow())
            }
            delete {
                val userId = call.parameters.getLong("id") { UserId(it) }
                userController.delete(call, userId)
            }
        }
        route("/recipes") {
            get {
                val afterId = call.request.queryParameters.getLongOrNull("after_id") { RecipeId(it) }
                val tagName = call.request.queryParameters.getStringOrNull("tag_name") { TagName(it) }
                recipesController.index(call, afterId, tagName)
            }
            post {
                recipesController.create(call, call.receiveOrThrow())
            }
        }
        route("/recipes/{id}") {
            get {
                val recipeId = call.parameters.getLong("id") { RecipeId(it) }
                recipeController.index(call, recipeId)
            }
            patch {
                val recipeId = call.parameters.getLong("id") { RecipeId(it) }
                recipeController.update(call, recipeId, call.receiveOrThrow())
            }
            delete {
                val recipeId = call.parameters.getLong("id") { RecipeId(it) }
                recipeController.delete(call, recipeId)
            }
        }
        route("/tags") {
            get {
                tagsController.index(call)
            }
            post {
                tagsController.create(call, call.receiveOrThrow())
            }
        }
        route("/tags/{name}") {
            get {
                val tagName = call.parameters.getString("name") { TagName(it) }
                tagController.index(call, tagName)
            }
            patch {
                val tagName = call.parameters.getString("name") { TagName(it) }
                tagController.update(call, tagName, call.receiveOrThrow())
            }
            delete {
                val tagName = call.parameters.getString("name") { TagName(it) }
                tagController.delete(call, tagName)
            }
        }
        route("/menus") {
            get {
                val afterId = call.request.queryParameters.getLongOrNull("after_id") { MenuId(it) }
                menusController.index(call, afterId)
            }
            post {
                menusController.create(call, call.receiveOrThrow())
            }
        }
        route("/menus/{id}") {
            get {
                val menuId = call.parameters.getLong("id") { MenuId(it) }
                menuController.index(call, menuId)
            }
            patch {
                val menuId = call.parameters.getLong("id") { MenuId(it) }
                menuController.update(call, menuId, call.receiveOrThrow())
            }
            delete {
                val menuId = call.parameters.getLong("id") { MenuId(it) }
                menuController.delete(call, menuId)
            }
        }
    }
}
