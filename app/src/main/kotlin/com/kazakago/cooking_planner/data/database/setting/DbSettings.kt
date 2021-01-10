package com.kazakago.cooking_planner.data.database.setting

import com.kazakago.cooking_planner.data.database.migration.Migrator
import org.jetbrains.exposed.sql.Database

object DbSettings {
    val db: Database by lazy {
        Database.connect(url = "jdbc:postgresql://localhost:5432/postgres", driver = "org.postgresql.Driver", user = "postgres", password = "password").apply {
            Migrator.execute()
        }
    }
}
