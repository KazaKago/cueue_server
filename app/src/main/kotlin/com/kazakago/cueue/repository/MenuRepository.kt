package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.MenuEntity
import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.MenusTable
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.mapper.rawValue
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.model.MenuUpdatingData
import com.kazakago.cueue.model.TimeFrame
import io.ktor.features.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class MenuRepository {

    suspend fun getMenus(workspace: WorkspaceEntity, afterId: MenuId?): List<MenuEntity> {
        return newSuspendedTransaction {
            val timeFrameExpression = Case()
                .When(Op.build { MenusTable.timeFrame eq TimeFrame.Dinner.rawValue() }, intLiteral(1))
                .When(Op.build { MenusTable.timeFrame eq TimeFrame.SnackTime.rawValue() }, intLiteral(2))
                .When(Op.build { MenusTable.timeFrame eq TimeFrame.Lunch.rawValue() }, intLiteral(3))
                .When(Op.build { MenusTable.timeFrame eq TimeFrame.Breakfast.rawValue() }, intLiteral(4))
                .Else(intLiteral(5))
            val menus = MenuEntity.find { MenusTable.workspaceId eq workspace.id.value }
                .orderBy(
                    MenusTable.date to SortOrder.DESC,
                    timeFrameExpression to SortOrder.ASC,
                    MenusTable.id to SortOrder.DESC
                )
            val offset = if (afterId != null) {
                val index = menus.indexOfFirst { it.id.value == afterId.value }
                if (index < 0) throw MissingRequestParameterException("after_id (${afterId.value})")
                index + 1L
            } else {
                0L
            }
            menus.limit(20, offset).toList()
        }
    }

    suspend fun getMenu(workspace: WorkspaceEntity, menuId: MenuId): MenuEntity {
        return newSuspendedTransaction {
            MenuEntity.find { (MenusTable.workspaceId eq workspace.id.value) and (MenusTable.id eq menuId.value) }.first()
        }
    }

    suspend fun createMenu(workspace: WorkspaceEntity, menu: MenuRegistrationData): MenuEntity {
        return newSuspendedTransaction {
            MenuEntity.new {
                this.memo = menu.memo ?: ""
                this.date = menu.date
                this.timeFrame = menu.timeFrame.rawValue()
                this.workspace = workspace
            }.apply {
                val rawRecipeIds = menu.recipeIds?.map { it.value } ?: emptyList()
                this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id inList rawRecipeIds) }
            }
        }
    }

    suspend fun updateMenu(workspace: WorkspaceEntity, menuId: MenuId, menu: MenuUpdatingData): MenuEntity {
        return newSuspendedTransaction {
            MenuEntity.find { (MenusTable.workspaceId eq workspace.id.value) and (MenusTable.id eq menuId.value) }.first().apply {
                menu.memo?.let { this.memo = it }
                menu.date?.let { this.date = it }
                menu.timeFrame?.let { this.timeFrame = it.rawValue() }
                menu.recipeIds?.let { recipeIds ->
                    val rawRecipeIds = recipeIds.map { it.value }
                    this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspace.id.value) and (RecipesTable.id inList rawRecipeIds) }
                }
                this.updatedAt = LocalDateTime.now()
            }
        }
    }

    suspend fun deleteMenu(workspace: WorkspaceEntity, menuId: MenuId) {
        newSuspendedTransaction {
            val menu = MenuEntity.find { (MenusTable.workspaceId eq workspace.id.value) and (MenusTable.id eq menuId.value) }.first()
            menu.delete()
        }
    }
}
