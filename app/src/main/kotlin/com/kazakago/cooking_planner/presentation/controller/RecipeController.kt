package com.kazakago.cooking_planner.presentation.controller

import com.kazakago.cooking_planner.domain.model.RecipeId
import com.kazakago.cooking_planner.data.repository.RecipeRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipeController(private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, recipeId: Long) {
        val recipe = recipeRepository.getRecipeTags(RecipeId(recipeId))
        call.respond(HttpStatusCode.OK, recipe)
    }

    suspend fun delete(call: ApplicationCall, recipeId: Long) {
        recipeRepository.deleteRecipe(RecipeId(recipeId))
        call.respond(HttpStatusCode.NoContent)
    }
}
