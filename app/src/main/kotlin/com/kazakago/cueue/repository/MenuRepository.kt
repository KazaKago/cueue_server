package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.MenuEntity
import com.kazakago.cueue.database.entity.RecipeSummaryEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.setting.DbSettings
import com.kazakago.cueue.database.table.MenusTable
import com.kazakago.cueue.mapper.rawValue
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class MenuRepository {

    suspend fun getMenus(workspace: WorkspaceEntity, afterId: MenuId?): List<MenuEntity> {
        return newSuspendedTransaction(db = DbSettings.db) {
            val menus = MenuEntity.find { MenusTable.workspaceId eq workspace.id.value }
                .orderBy(MenusTable.id to SortOrder.DESC)
            val offset = if (afterId != null) {
                menus.indexOfFirst { it.id.value == afterId.value }.toLong() + 1
            } else {
                0
            }
            menus.limit(20, offset).toList()
        }
    }

    suspend fun getMenu(workspace: WorkspaceEntity, menuId: MenuId): MenuEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            MenuEntity.find { (MenusTable.workspaceId eq workspace.id.value) and (MenusTable.id eq menuId.value) }.first()
        }
    }

    suspend fun createMenu(workspace: WorkspaceEntity, menu: MenuRegistrationData): MenuEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            MenuEntity.new {
                this.memo = menu.memo
                this.date = menu.date
                this.timeFrame = menu.timeFrame.rawValue()
                this.workspace = workspace
            }.apply {
                val rawRecipeIds = menu.recipeIds.map { it.value }
                this.recipes = RecipeSummaryEntity.forIds(rawRecipeIds)
            }
        }
    }

    suspend fun updateMenu(workspace: WorkspaceEntity, menuId: MenuId, menu: MenuRegistrationData): MenuEntity {
        return newSuspendedTransaction(db = DbSettings.db) {
            MenuEntity.find { (MenusTable.workspaceId eq workspace.id.value) and (MenusTable.id eq menuId.value) }.first().apply {
                this.memo = menu.memo
                this.date = menu.date
                this.timeFrame = menu.timeFrame.rawValue()
                this.updatedAt = LocalDateTime.now()
                this.workspace = workspace
                val rawRecipeIds = menu.recipeIds.map { it.value }
                this.recipes = RecipeSummaryEntity.forIds(rawRecipeIds)
            }
        }
    }

    suspend fun deleteMenu(workspace: WorkspaceEntity, menuId: MenuId) {
        newSuspendedTransaction(db = DbSettings.db) {
            val menu = MenuEntity.find { (MenusTable.workspaceId eq workspace.id.value) and (MenusTable.id eq menuId.value) }.first()
            menu.delete()
        }
    }
}
