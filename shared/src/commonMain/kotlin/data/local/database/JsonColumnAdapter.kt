package com.attendace.leopard.data.local.database

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class JsonColumnAdapter<T : Any>(val type: KClass<T>) : ColumnAdapter<T, String> {

    @OptIn(InternalSerializationApi::class)
    override fun decode(databaseValue: String): T =
        Json.decodeFromString(string = databaseValue, deserializer = type.serializer())

    @OptIn(InternalSerializationApi::class)
    override fun encode(value: T) =
        Json.encodeToString(serializer = type.serializer(), value = value)
}

/** A [ColumnAdapter] which maps the class `T` to a string in the database. */
@Suppress("FunctionName") // Emulating a constructor.
inline fun <reified T : Any> JsonColumnAdapter(): JsonColumnAdapter<T> {
    return JsonColumnAdapter(T::class)
}