package core.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

// This needs to be initialized via Koin or Context injection
lateinit var appContext: Context

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val dbFile = context.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    // Relying on global context for now, ideally passed via DI
     val dbFile = appContext.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
