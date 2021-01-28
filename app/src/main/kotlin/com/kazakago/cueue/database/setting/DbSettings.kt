package com.kazakago.cueue.database.setting

import com.kazakago.cueue.database.migration.Migrator
import org.jetbrains.exposed.sql.Database

object DbSettings {

    private var _db: Database? = null
    val db: Database by lazy { _db!! }

    fun initialize(url: String, user: String, password: String) {
        _db = Database.connect(url = url, driver = "org.postgresql.Driver", user = user, password = password)
        Migrator.execute()
    }
}