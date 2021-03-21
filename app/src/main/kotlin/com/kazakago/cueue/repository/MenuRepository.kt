package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.MenuEntity
import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.MenusTable
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.mapper.MenuMapper
import com.kazakago.cueue.mapper.rawValue
import com.kazakago.cueue.model.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class MenuRepository(private val menuMapper: MenuMapper) {

    suspend fun getMenus(workspaceId: WorkspaceId, afterId: MenuId?): List<Menu> {
        return newSuspendedTransaction {
            val timeFrameExpression = Case()
                .When(Op.build { MenusTable.timeFrame eq TimeFrame.Dinner.rawValue() }, intLiteral(1))
                .When(Op.build { MenusTable.timeFrame eq TimeFrame.SnackTime.rawValue() }, intLiteral(2))
                .When(Op.build { MenusTable.timeFrame eq TimeFrame.Lunch.rawValue() }, intLiteral(3))
                .When(Op.build { MenusTable.timeFrame eq TimeFrame.Breakfast.rawValue() }, intLiteral(4))
                .Else(intLiteral(5))
            val menus = MenuEntity.find { MenusTable.workspaceId eq workspaceId.value }
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
            val entities = menus.limit(20, offset).toList()
            entities.map { menuMapper.toModel(it) }
        }
    }

    suspend fun getMenu(workspaceId: WorkspaceId, menuId: MenuId): Menu {
        return newSuspendedTransaction {
            val entity = MenuEntity.find { (MenusTable.workspaceId eq workspaceId.value) and (MenusTable.id eq menuId.value) }.first()
            menuMapper.toModel(entity)
        }
    }

    suspend fun createMenu(workspaceId: WorkspaceId, menu: MenuRegistrationData): Menu {
        return newSuspendedTransaction {
            val entity = MenuEntity.new {
                this.memo = menu.memo ?: ""
                this.date = menu.date
                this.timeFrame = menu.timeFrame.rawValue()
                this.workspace = WorkspaceEntity[workspaceId.value]
            }.apply {
                val rawRecipeIds = menu.recipeIds?.map { it.value } ?: emptyList()
                this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id inList rawRecipeIds) }
            }
            menuMapper.toModel(entity)
        }
    }

    suspend fun updateMenu(workspaceId: WorkspaceId, menuId: MenuId, menu: MenuRegistrationData): Menu {
        return newSuspendedTransaction {
            val entity = MenuEntity.find { (MenusTable.workspaceId eq workspaceId.value) and (MenusTable.id eq menuId.value) }.first().apply {
                this.memo = menu.memo ?: ""
                this.date = menu.date
                this.timeFrame = menu.timeFrame.rawValue()
                val rawRecipeIds = menu.recipeIds?.map { it.value } ?: emptyList()
                this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id inList rawRecipeIds) }
                this.updatedAt = LocalDateTime.now()
            }
            menuMapper.toModel(entity)
        }
    }

    suspend fun deleteMenu(workspaceId: WorkspaceId, menuId: MenuId) {
        newSuspendedTransaction {
            val menu = MenuEntity.find { (MenusTable.workspaceId eq workspaceId.value) and (MenusTable.id eq menuId.value) }.first()
            menu.delete()
        }
    }
}
