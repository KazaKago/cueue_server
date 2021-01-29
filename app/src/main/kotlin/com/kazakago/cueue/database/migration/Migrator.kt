package com.kazakago.cueue.database.migration

import com.kazakago.cueue.database.table.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Migrator {

    fun execute() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(RecipesTable)
            SchemaUtils.createMissingTablesAndColumns(TagsTable)
            SchemaUtils.createMissingTablesAndColumns(RecipeTagsRelationsTable)
            SchemaUtils.createMissingTablesAndColumns(MenusTable)
            SchemaUtils.createMissingTablesAndColumns(MenuRecipesRelationsTable)
            SchemaUtils.createMissingTablesAndColumns(UsersTable)
            SchemaUtils.createMissingTablesAndColumns(WorkspacesTable)
            SchemaUtils.createMissingTablesAndColumns(UserWorkspacesRelationsTable)
        }
    }
}
