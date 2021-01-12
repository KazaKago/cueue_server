package com.kazakago.cooking_planner.route

import com.kazakago.cooking_planner.controller.*
import com.kazakago.cooking_planner.mapper.*
import com.kazakago.cooking_planner.model.MenuId
import com.kazakago.cooking_planner.model.RecipeId
import com.kazakago.cooking_planner.model.TagName
import com.kazakago.cooking_planner.repository.MenuRepository
import com.kazakago.cooking_planner.repository.RecipeRepository
import com.kazakago.cooking_planner.repository.TagRepository
import com.kazakago.cooking_planner.repository.UserRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.appRouting() {
    val rootController = RootController()
    val userController = UsersController(UserRepository(UserMapper()))
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
                userController.index(call)
            }
            post {
                userController.create(call)
            }
        }
        route("/recipes") {
            get {
                val afterId = call.request.queryParameters.getLongOrNull("after_id")?.let { RecipeId(it) }
                val tagName = call.request.queryParameters.getStringOrNull("tag_name")?.let { TagName(it) }
                recipesController.index(call, afterId, tagName)
            }
            post {
                recipesController.create(call, call.receive())
            }
        }
        route("/recipes/{id}") {
            get {
                val recipeId = call.parameters.getLong("id").let { RecipeId(it) }
                recipeController.index(call, recipeId)
            }
            delete {
                val recipeId = call.parameters.getLong("id").let { RecipeId(it) }
                recipeController.delete(call, recipeId)
            }
        }
        route("/tags") {
            get {
                tagsController.index(call)
            }
            post {
                tagsController.create(call, call.receive())
            }
        }
        route("/tags/{name}") {
            get {
                val tagName = call.parameters.getString("name").let { TagName(it) }
                tagController.index(call, tagName)
            }
            delete {
                val tagName = call.parameters.getString("name").let { TagName(it) }
                tagController.delete(call, tagName)
            }
        }
        route("/menus") {
            get {
                val afterId = call.request.queryParameters.getLongOrNull("after_id")?.let { MenuId(it) }
                menusController.index(call, afterId)
            }
            post {
                menusController.create(call, call.receive())
            }
        }
        route("/menus/{id}") {
            get {
                val menuId = call.parameters.getLong("id").let { MenuId(it) }
                menuController.index(call, menuId)
            }
            delete {
                val menuId = call.parameters.getLong("id").let { MenuId(it) }
                menuController.delete(call, menuId)
            }
        }
    }
}
