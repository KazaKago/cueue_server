package com.kazakago.cueue.database.entity

import com.kazakago.cueue.database.table.ContentsTable
import com.kazakago.cueue.database.table.MenuRecipesRelationsTable
import com.kazakago.cueue.database.table.RecipeTagsRelationsTable
import com.kazakago.cueue.database.table.RecipesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class RecipeEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RecipeEntity>(RecipesTable)

    var title by RecipesTable.title
    var kana by RecipesTable.kana
    var description by RecipesTable.description
    var url by RecipesTable.url
    val images by ContentEntity optionalReferrersOn ContentsTable.recipeId
    var createdAt by RecipesTable.createdAt
    var updatedAt by RecipesTable.updatedAt
    var workspace by WorkspaceEntity referencedOn RecipesTable.workspaceId
    var tags by TagEntity via RecipeTagsRelationsTable
    var menus by MenuEntity via MenuRecipesRelationsTable
}
