package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class RecipeController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val recipe = recipeRepository.getRecipe(user.requireWorkspace().id, recipeId)
        call.respond(HttpStatusCode.OK, recipe)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId, recipeRegistrationData: RecipeRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val recipe = recipeRepository.updateRecipe(user.requireWorkspace().id, recipeId, recipeRegistrationData)
        call.respond(HttpStatusCode.OK, recipe)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId) {
        val user = userRepository.getUser(firebaseUser.uid)
        recipeRepository.deleteRecipe(user.requireWorkspace().id, recipeId)
        call.respond(HttpStatusCode.NoContent)
    }
}
