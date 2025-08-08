package com.luisma.pactype.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.luisma.database.PactypeDB

actual fun getSqlDriver() : SqlDriver {
    return  NativeSqliteDriver(
        schema = PactypeDB.Schema,
        name = "pactype.db"
    )
}