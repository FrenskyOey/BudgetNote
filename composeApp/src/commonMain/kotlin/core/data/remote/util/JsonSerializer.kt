package core.data.remote.util

import kotlinx.serialization.json.Json

object JsonSerializer {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = false
        coerceInputValues = true
    }
}
