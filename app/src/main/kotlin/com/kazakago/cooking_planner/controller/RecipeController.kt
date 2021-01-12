package com.kazakago.cooking_planner.controller

import com.kazakago.cooking_planner.model.RecipeId
import com.kazakago.cooking_planner.repository.RecipeRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipeController(private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, recipeId: RecipeId) {
        val recipe = recipeRepository.getRecipe(recipeId)
        call.respond(HttpStatusCode.OK, recipe)
    }

    suspend fun delete(call: ApplicationCall, recipeId: RecipeId) {
        recipeRepository.deleteRecipe(recipeId)
        call.respond(HttpStatusCode.NoContent)
    }
}
