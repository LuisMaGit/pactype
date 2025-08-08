package com.luisma.pactype.services.db_services

import com.luisma.database.PactypeDB
import com.luisma.pactype.platform.getSqlDriver

object DBInstance {
    val db = PactypeDB(getSqlDriver())
}