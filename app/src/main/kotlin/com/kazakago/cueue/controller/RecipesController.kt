package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipesController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, afterId: RecipeId?, tagId: TagId?) {
        val user = userRepository.getUser(firebaseUser.uid)
        val models = recipeRepository.getRecipes(user.personalWorkspace.id, afterId, tagId)
        call.respond(HttpStatusCode.OK, models)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, recipe: RecipeRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val model = recipeRepository.createRecipe(user.personalWorkspace.id, recipe)
        call.respond(HttpStatusCode.Created, model)
    }
}
