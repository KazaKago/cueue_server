package com.kazakago.cueue.controller

import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.TagName
import com.kazakago.cueue.repository.RecipeRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipesController(private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, afterId: RecipeId?, tagName: TagName?) {
        val recipes = recipeRepository.getRecipes(afterId, tagName)
        call.respond(HttpStatusCode.OK, recipes)
    }

    suspend fun create(call: ApplicationCall, recipe: RecipeRegistrationData) {
        recipeRepository.createRecipe(recipe)
        call.respond(HttpStatusCode.Created)
    }
}
