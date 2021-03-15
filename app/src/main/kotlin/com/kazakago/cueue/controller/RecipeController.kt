package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeUpdatingData
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipeController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = recipeRepository.getRecipe(user.personalWorkspace.id, recipeId)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId, recipe: RecipeUpdatingData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = recipeRepository.updateRecipe(user.personalWorkspace.id, recipeId, recipe)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId) {
        val user = userRepository.getUser(firebaseUser.uid)
        recipeRepository.deleteRecipe(user.personalWorkspace.id, recipeId)
        call.respond(HttpStatusCode.NoContent)
    }
}
