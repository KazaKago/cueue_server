package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.WorkspaceId
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipeController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, recipeId: RecipeId) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        val model = recipeRepository.getRecipe(workspaceId, recipeId)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun update(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, recipeId: RecipeId, recipe: RecipeRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        val model = recipeRepository.updateRecipe(workspaceId, recipeId, recipe)
        call.respond(HttpStatusCode.OK, model)
    }

    suspend fun delete(call: ApplicationCall, firebaseUser: FirebaseUser, workspaceId: WorkspaceId, recipeId: RecipeId) {
        val user = userRepository.getUser(firebaseUser.uid)
        user.validate(workspaceId)
        recipeRepository.deleteRecipe(workspaceId, recipeId)
        call.respond(HttpStatusCode.NoContent)
    }
}
