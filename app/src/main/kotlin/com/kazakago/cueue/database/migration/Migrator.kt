package com.kazakago.cueue.database.migration

import com.kazakago.cueue.database.table.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Migrator {

    fun execute() {
        transaction {
            SchemaUtils.create(UsersTable)
            SchemaUtils.create(RecipesTable)
            SchemaUtils.create(TagsTable)
            SchemaUtils.create(RecipeTagsRelationsTable)
            SchemaUtils.create(MenusTable)
            SchemaUtils.create(MenuRecipesRelationsTable)
        }
    }
}
