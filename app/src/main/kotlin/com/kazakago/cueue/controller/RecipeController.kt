package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.RecipeMapper
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipeController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository, private val recipeMapper: RecipeMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId) {
        val user = userRepository.getUser(firebaseUser.uid)
        val recipe = recipeRepository.getRecipe(user.defaultWorkSpace(), recipeId)
        call.respond(HttpStatusCode.OK, recipeMapper.toModel(recipe))
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId, recipe: RecipeRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        recipeRepository.updateRecipe(user.defaultWorkSpace(), recipeId, recipe)
        call.respond(HttpStatusCode.NoContent)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, recipeId: RecipeId) {
        val user = userRepository.getUser(firebaseUser.uid)
        recipeRepository.deleteRecipe(user.defaultWorkSpace(), recipeId)
        call.respond(HttpStatusCode.NoContent)
    }
}
