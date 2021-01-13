package com.kazakago.cooking_planner.repository

import com.kazakago.cooking_planner.database.entity.MenuEntity
import com.kazakago.cooking_planner.database.entity.RecipeSummaryEntity
import com.kazakago.cooking_planner.database.setting.DbSettings
import com.kazakago.cooking_planner.database.table.MenusTable
import com.kazakago.cooking_planner.mapper.MenuMapper
import com.kazakago.cooking_planner.mapper.rawValue
import com.kazakago.cooking_planner.model.Menu
import com.kazakago.cooking_planner.model.MenuId
import com.kazakago.cooking_planner.model.MenuRegistrationData
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

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
            val menu = MenuEntity[menuId.value]
            menuMapper.toModel(menu)
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
                recipes = RecipeSummaryEntity.forIds(rawRecipeIds)
            }
        }
    }

    suspend fun updateMenu(menuId: MenuId, menu: MenuRegistrationData) {
        newSuspendedTransaction(db = DbSettings.db) {
            MenuEntity[menuId.value].apply {
                memo = menu.memo
                date = menu.date
                timeFrame = menu.timeFrame.rawValue()
                updatedAt = LocalDateTime.now()
                val rawRecipeIds = menu.recipeIds.map { it.value }
                recipes = RecipeSummaryEntity.forIds(rawRecipeIds)
            }
        }
    }

    suspend fun deleteMenu(menuId: MenuId) {
        newSuspendedTransaction(db = DbSettings.db) {
            val menu = MenuEntity[menuId.value]
            menu.delete()
        }
    }
}
