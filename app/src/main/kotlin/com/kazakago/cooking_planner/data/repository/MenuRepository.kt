package com.kazakago.cooking_planner.data.repository

import com.kazakago.cooking_planner.data.database.entity.MenuEntity
import com.kazakago.cooking_planner.data.database.entity.RecipeEntity
import com.kazakago.cooking_planner.data.database.setting.DbSettings
import com.kazakago.cooking_planner.data.mapper.MenuRecipesMapper
import com.kazakago.cooking_planner.data.mapper.rawValue
import com.kazakago.cooking_planner.domain.model.MenuId
import com.kazakago.cooking_planner.domain.model.MenuRecipes
import com.kazakago.cooking_planner.domain.model.MenuRegistrationData
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MenuRepository(private val menuRecipesMapper: MenuRecipesMapper) {

    suspend fun getMenuRecipesList(): List<MenuRecipes> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val recipes = MenuEntity.all()
            recipes.map { menuRecipesMapper.toModel(it) }
        }
    }

    suspend fun getMenuRecipes(menuId: MenuId): MenuRecipes {
        return newSuspendedTransaction(db = DbSettings.db) {
            val recipe = MenuEntity.findById(menuId.value) ?: throw NoSuchElementException()
            menuRecipesMapper.toModel(recipe)
        }
    }

    suspend fun createMenu(menu: MenuRegistrationData) {
        newSuspendedTransaction(db = DbSettings.db) {
            MenuEntity.new {
                memo = menu.memo
                date = menu.date
                timeFrame = menu.timeFrame.rawValue()
            }.apply {
                val rawRecipeIds = menu.recipeIds.map { it.value }
                recipes = RecipeEntity.forIds(rawRecipeIds)
            }
        }
    }

    suspend fun deleteMenu(menuId: MenuId) {
        newSuspendedTransaction(db = DbSettings.db) {
            val recipe = RecipeEntity.findById(menuId.value) ?: throw NoSuchElementException()
            recipe.delete()
        }
    }
}
