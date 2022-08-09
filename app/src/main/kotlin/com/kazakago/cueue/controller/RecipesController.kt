package com.kazakago.cueue.controller

import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class RecipesController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, afterId: RecipeId?, keyword: String?, tagIds: List<TagId>?) {
        val user = userRepository.getUser(firebaseUser.uid)
        val recipes = recipeRepository.getRecipes(user.requireWorkspace().id, afterId, keyword, tagIds)
        call.respond(HttpStatusCode.OK, recipes)
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, recipe: RecipeRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val recipe = recipeRepository.createRecipe(user.requireWorkspace().id, recipe)
        call.respond(HttpStatusCode.Created, recipe)
    }
}
