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
    val userController = UserController(UserRepository(UserMapper()))
    val recipesController = RecipesController(RecipeRepository(RecipeTagsMapper(TagMapper())))
    val recipeController = RecipeController(RecipeRepository(RecipeTagsMapper(TagMapper())))
    val tagsController = TagsController(TagRepository(TagMapper(), TagRecipesMapper(RecipeMapper())))
    val tagController = TagController(TagRepository(TagMapper(), TagRecipesMapper(RecipeMapper())))
    val menusController = MenusController(MenuRepository(MenuRecipesMapper(TimeFrameMapper(), RecipeMapper())))
    val menuController = MenuController(MenuRepository(MenuRecipesMapper(TimeFrameMapper(), RecipeMapper())))
    routing {
        route("/") {
            get { rootController.index(call) }
        }
        route("/users") {
            get { userController.index(call) }
            post { userController.create(call) }
        }
        route("/recipes") {
            get { recipesController.index(call) }
            post { recipesController.create(call, call.receive()) }
        }
        route("/recipes/{id}") {
            get { call.parameters.getAsLong("id") { recipeController.index(call, RecipeId(it)) } }
            delete { call.parameters.getAsLong("id") { recipeController.delete(call, RecipeId(it)) } }
        }
        route("/tags") {
            get { tagsController.index(call) }
            post { tagsController.create(call, call.receive()) }
        }
        route("/tags/{name}") {
            get { call.parameters.getAsString("name") { tagController.index(call, TagName(it)) } }
            delete { call.parameters.getAsString("name") { tagController.delete(call, TagName(it)) } }
        }
        route("/menus") {
            get { menusController.index(call) }
            post { menusController.create(call, call.receive()) }
        }
        route("/menus/{id}") {
            get { call.parameters.getAsLong("id") { menuController.index(call, MenuId(it)) } }
            delete { call.parameters.getAsLong("id") { menuController.delete(call, MenuId(it)) } }
        }
    }
}
