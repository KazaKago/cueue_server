package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.model.RecipeId
import com.kazakago.cooking_planner.model.RecipeRegistrationData
import com.kazakago.cooking_planner.model.TagName
import com.kazakago.cooking_planner.repository.RecipeRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipesController(private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, afterId: RecipeId?, tagName: TagName?) {
        val recipes = recipeRepository.getRecipeTagsList(afterId, tagName)
        call.respond(HttpStatusCode.OK, recipes)
    }

    suspend fun create(call: ApplicationCall, recipe: RecipeRegistrationData) {
        recipeRepository.createRecipe(recipe)
        call.respond(HttpStatusCode.Created)
    }
}
