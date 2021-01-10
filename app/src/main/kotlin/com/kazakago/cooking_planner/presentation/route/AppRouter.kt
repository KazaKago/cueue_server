package com.kazakago.cooking_planner.presentation.route

import com.kazakago.cooking_planner.presentation.controller.*
import com.kazakago.cooking_planner.data.mapper.*
import com.kazakago.cooking_planner.data.repository.RecipeRepository
import com.kazakago.cooking_planner.data.repository.TagRepository
import com.kazakago.cooking_planner.data.repository.UserRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

fun Application.appRouting() {
    val rootController = RootController()
    val userController = UserController(UserRepository(UserMapper()))
    val recipesController = RecipesController(RecipeRepository(RecipeTagsMapper()))
    val recipeController = RecipeController(RecipeRepository(RecipeTagsMapper()))
    val tagsController = TagsController(TagRepository(TagMapper(), TagRecipesMapper(RecipeMapper())))
    val tagController = TagController(TagRepository(TagMapper(), TagRecipesMapper(RecipeMapper())))
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
            get { call.parameters.getAsLong("id") { recipeController.index(call, it) } }
            delete { call.parameters.getAsLong("id") { recipeController.delete(call, it) } }
        }
        route("/tags") {
            get { tagsController.index(call) }
            post { tagsController.create(call, call.receive()) }
        }
        route("/tags/{name}") {
            get { call.parameters.getAsString("name") { tagController.index(call, it) } }
            delete { call.parameters.getAsString("name") { tagController.delete(call, it) } }
        }
    }
}
