package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.MenuEntity
import com.kazakago.cueue.database.entity.RecipeSummaryEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.MenusTable
import com.kazakago.cueue.mapper.MenuMapper
import com.kazakago.cueue.mapper.rawValue
import com.kazakago.cueue.model.Menu
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
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
        }.map {
            menuMapper.toModel(it)
        }
    }

    suspend fun getMenu(menuId: MenuId): Menu {
        return newSuspendedTransaction(db = DbSettings.db) {
            MenuEntity[menuId.value]
        }.let {
            menuMapper.toModel(it)
        }
    }

    suspend fun createMenu(menu: MenuRegistrationData): Menu {
        return newSuspendedTransaction(db = DbSettings.db) {
            MenuEntity.new {
                memo = menu.memo
                date = menu.date
                timeFrame = menu.timeFrame.rawValue()
            }.apply {
                val rawRecipeIds = menu.recipeIds.map { it.value }
                recipes = RecipeSummaryEntity.forIds(rawRecipeIds)
            }
        }.let {
            menuMapper.toModel(it)
        }
    }

    suspend fun updateMenu(menuId: MenuId, menu: MenuRegistrationData): Menu {
        return newSuspendedTransaction(db = DbSettings.db) {
            MenuEntity[menuId.value].apply {
                memo = menu.memo
                date = menu.date
                timeFrame = menu.timeFrame.rawValue()
                updatedAt = LocalDateTime.now()
                val rawRecipeIds = menu.recipeIds.map { it.value }
                recipes = RecipeSummaryEntity.forIds(rawRecipeIds)
            }
        }.let {
            menuMapper.toModel(it)
        }
    }

    suspend fun deleteMenu(menuId: MenuId) {
        newSuspendedTransaction(db = DbSettings.db) {
            val menu = MenuEntity[menuId.value]
            menu.delete()
        }
    }
}
