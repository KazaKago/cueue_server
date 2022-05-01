package com.kazakago.cueue.controller

import com.kazakago.cueue.model.*
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class RecipesController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, unsafeWorkspaceId: UnsafeWorkspaceId, afterId: RecipeId?, keyword: String?, tagIds: List<TagId>?) {
        val user = userRepository.getUser(firebaseUser.uid)
        val workspaceId = unsafeWorkspaceId.validate(user)
        val models = recipeRepository.getRecipes(workspaceId, afterId, keyword, tagIds)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, unsafeWorkspaceId: UnsafeWorkspaceId, recipe: RecipeRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val workspaceId = unsafeWorkspaceId.validate(user)
        val model = recipeRepository.createRecipe(workspaceId, recipe)
        call.respond(HttpStatusCode.Created, model)
    }
}
