package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.RecipeMapper
import com.kazakago.cueue.model.*
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipesController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository, private val recipeMapper: RecipeMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, afterId: RecipeId?, tagId: TagId?) {
        val user = userRepository.getUser(firebaseUser.uid)
        val recipes = recipeRepository.getRecipes(user.personalWorkSpace(), afterId, tagId)
        call.respond(HttpStatusCode.OK, recipes.map { recipeMapper.toModel(it) })
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, recipe: RecipeRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        recipeRepository.createRecipe(user.personalWorkSpace(), recipe)
        call.respond(HttpStatusCode.Created)
    }
}
