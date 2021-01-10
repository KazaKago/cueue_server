package com.kazakago.cooking_planner.data.database.entity

import com.kazakago.cooking_planner.data.database.table.MenuRecipesRelationsTable
import com.kazakago.cooking_planner.data.database.table.RecipeTagsRelationsTable
import com.kazakago.cooking_planner.data.database.table.RecipesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RecipeEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RecipeEntity>(RecipesTable)

    var title by RecipesTable.title
    var description by RecipesTable.description
    var tags by TagEntity via RecipeTagsRelationsTable
    var menus by MenuEntity via MenuRecipesRelationsTable
}
