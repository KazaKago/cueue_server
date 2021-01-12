package com.kazakago.cooking_planner.repository

import com.kazakago.cooking_planner.database.entity.MenuEntity
import com.kazakago.cooking_planner.database.entity.RecipeEntity
import com.kazakago.cooking_planner.database.setting.DbSettings
import com.kazakago.cooking_planner.database.table.MenusTable
import com.kazakago.cooking_planner.mapper.MenuMapper
import com.kazakago.cooking_planner.mapper.rawValue
import com.kazakago.cooking_planner.model.MenuId
import com.kazakago.cooking_planner.model.Menu
import com.kazakago.cooking_planner.model.MenuRegistrationData
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MenuRepository(private val menuMapper: MenuMapper) {

    suspend fun getMenus(afterId: MenuId?): List<Menu> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val menus = MenuEntity.all()
                .orderBy(MenusTable.id to SortOrder.DESC)
            val offset = if (afterId != null) {
                menus.indexOfFirst { it.id.value == afterId.value }.toLong() + 1
            } else {
                0
            }
            menus.limit(20, offset)
                .map { menuMapper.toModel(it) }
        }
    }

    suspend fun getMenu(menuId: MenuId): Menu {
        return newSuspendedTransaction(db = DbSettings.db) {
            val recipe = MenuEntity.findById(menuId.value) ?: throw NoSuchElementException()
            menuMapper.toModel(recipe)
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
