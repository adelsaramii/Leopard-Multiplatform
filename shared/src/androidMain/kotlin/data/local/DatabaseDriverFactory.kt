package com.attendace.leopard.data.local

import android.content.Context
import com.attendace.leopard.LeopardDb
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(LeopardDb.Schema, context, "Leopard.db")
    }
}