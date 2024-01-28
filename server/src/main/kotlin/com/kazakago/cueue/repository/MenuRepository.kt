package com.kazakago.cueue.repository

import com.kazakago.cueue.database.entity.MenuEntity
import com.kazakago.cueue.database.entity.RecipeEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.MenusTable
import com.kazakago.cueue.database.table.RecipesTable
import com.kazakago.cueue.mapper.MenuMapper
import com.kazakago.cueue.mapper.MenuSummaryMapper
import com.kazakago.cueue.mapper.rawValue
import com.kazakago.cueue.model.Menu
import com.kazakago.cueue.model.MenuId
import com.kazakago.cueue.model.MenuRegistrationData
import com.kazakago.cueue.model.MenuSummary
import com.kazakago.cueue.model.TimeFrame
import com.kazakago.cueue.model.WorkspaceId
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.Case
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.intLiteral
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MenuRepository(private val menuMapper: MenuMapper, private val menuSummaryMapper: MenuSummaryMapper) {

    suspend fun getMenus(workspaceId: WorkspaceId, data: LocalDate): List<MenuSummary> {
        return newSuspendedTransaction {
            val timeFrameExpression = getTimeFrameExpression()
            MenuEntity
                .find { (MenusTable.workspaceId eq workspaceId.value) and (MenusTable.date eq data) }
                .orderBy(MenusTable.date to SortOrder.DESC)
                .orderBy(timeFrameExpression to SortOrder.ASC)
                .orderBy(MenusTable.id to SortOrder.DESC)
                .with(MenuEntity::recipes, RecipeEntity::images, RecipeEntity::menus)
                .map { menuSummaryMapper.toModel(it) }
        }
    }

    suspend fun getMenu(workspaceId: WorkspaceId, menuId: MenuId): Menu {
        return newSuspendedTransaction {
            val entity = MenuEntity
                .find { (MenusTable.workspaceId eq workspaceId.value) and (MenusTable.id eq menuId.value) }
                .first()
                .load(MenuEntity::recipes, RecipeEntity::images, RecipeEntity::menus)
            menuMapper.toModel(entity)
        }
    }

    suspend fun createMenu(workspaceId: WorkspaceId, menu: MenuRegistrationData): Menu {
        return newSuspendedTransaction {
            val entity = MenuEntity
                .new {
                    this.memo = menu.memo ?: ""
                    this.date = menu.date
                    this.timeFrame = menu.timeFrame.rawValue()
                    this.workspace = WorkspaceEntity[workspaceId.value]
                }
                .apply {
                    val rawRecipeIds = menu.recipeIds?.map { it.value } ?: emptyList()
                    this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id inList rawRecipeIds) }
                }
            menuMapper.toModel(entity)
        }
    }

    suspend fun updateMenu(workspaceId: WorkspaceId, menuId: MenuId, menu: MenuRegistrationData): Menu {
        return newSuspendedTransaction {
            val entity = MenuEntity
                .find { (MenusTable.workspaceId eq workspaceId.value) and (MenusTable.id eq menuId.value) }
                .first()
                .load(MenuEntity::recipes, RecipeEntity::images, RecipeEntity::menus)
                .apply {
                    this.memo = menu.memo ?: ""
                    this.date = menu.date
                    this.timeFrame = menu.timeFrame.rawValue()
                    val rawRecipeIds = menu.recipeIds?.map { it.value } ?: emptyList()
                    this.recipes = RecipeEntity.find { (RecipesTable.workspaceId eq workspaceId.value) and (RecipesTable.id inList rawRecipeIds) }
                    this.updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC)
                }
            menuMapper.toModel(entity)
        }
    }

    suspend fun deleteMenu(workspaceId: WorkspaceId, menuId: MenuId) {
        newSuspendedTransaction {
            MenuEntity
                .find { (MenusTable.workspaceId eq workspaceId.value) and (MenusTable.id eq menuId.value) }
                .first()
                .delete()
        }
    }

    private fun getTimeFrameExpression(): Expression<Int> {
        return Case()
            .When(Op.build { MenusTable.timeFrame eq TimeFrame.Dinner.rawValue() }, intLiteral(1))
            .When(Op.build { MenusTable.timeFrame eq TimeFrame.SnackTime.rawValue() }, intLiteral(2))
            .When(Op.build { MenusTable.timeFrame eq TimeFrame.Lunch.rawValue() }, intLiteral(3))
            .When(Op.build { MenusTable.timeFrame eq TimeFrame.Breakfast.rawValue() }, intLiteral(4))
            .Else(intLiteral(5))
    }
}
