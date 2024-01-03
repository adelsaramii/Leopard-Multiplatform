package com.attendace.leopard.data.local

import com.attendace.leopard.LeopardDb
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(LeopardDb.Schema, "Leopard.db")
    }
}