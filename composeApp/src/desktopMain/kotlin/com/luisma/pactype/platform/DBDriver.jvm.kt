package com.luisma.pactype.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.luisma.database.PactypeDB
import java.io.File

actual fun getSqlDriver(): SqlDriver {
    val dbRootPath = File(System.getProperty("user.home"))
    //TODO: improve this, create app folder for mac, handle windows and linux
    val dbPath = "${dbRootPath.absolutePath}/Library/Application Support/pactype.db"
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:$dbPath")
    if (!File(dbPath).exists()) {
        PactypeDB.Schema.create(driver)
    }
    return driver
}