package com.luisma.pactype.platform

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.luisma.database.PactypeDB
import com.luisma.pactype.application

class DatabaseDriver(private val context: Context) {
    fun sqlDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = PactypeDB.Schema,
            context = context,
            name = "pactype.db"
        )
    }
}

actual fun getSqlDriver(): SqlDriver =
    DatabaseDriver(context = application).sqlDriver()