package com.kazakago.cueue.controller

import com.kazakago.cueue.mapper.RecipeMapper
import com.kazakago.cueue.model.FirebaseUser
import com.kazakago.cueue.model.RecipeId
import com.kazakago.cueue.model.RecipeRegistrationData
import com.kazakago.cueue.model.TagId
import com.kazakago.cueue.repository.RecipeRepository
import com.kazakago.cueue.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class RecipesController(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository, private val recipeMapper: RecipeMapper) {

    suspend fun index(call: ApplicationCall, firebaseUser: FirebaseUser, afterId: RecipeId?, tagId: TagId?) {
        val user = userRepository.getUser(firebaseUser.uid)
        val entities = recipeRepository.getRecipes(user.personalWorkSpace(), afterId, tagId)
        call.respond(HttpStatusCode.OK, entities.map { recipeMapper.toModel(it) })
    }

    suspend fun create(call: ApplicationCall, firebaseUser: FirebaseUser, recipe: RecipeRegistrationData) {
        val user = userRepository.getUser(firebaseUser.uid)
        val entity = recipeRepository.createRecipe(user.personalWorkSpace(), recipe)
        call.respond(HttpStatusCode.Created, recipeMapper.toModel(entity))
    }
}
