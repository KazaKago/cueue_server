package com.kazakago.cueue.controller

import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.repository.RecipeRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipeController(private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, recipeId: RecipeId) {
        val recipe = recipeRepository.getRecipe(recipeId)
        call.respond(HttpStatusCode.OK, recipe)
    }

    suspend fun update(call: ApplicationCall, recipeId: RecipeId, recipe: RecipeRegistrationData) {
        recipeRepository.updateRecipe(recipeId, recipe)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun delete(call: ApplicationCall, recipeId: RecipeId) {
        recipeRepository.deleteRecipe(recipeId)
        call.respond(HttpStatusCode.NoContent)
    }
}
