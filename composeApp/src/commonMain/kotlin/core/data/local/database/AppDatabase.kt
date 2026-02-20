package core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

import androidx.room.ConstructedBy
import androidx.room.RoomDatabaseConstructor
import core.data.local.database.dao.SampleDao
import core.data.local.database.entity.SampleEntity

// Placeholder for now. We will add entities later.
@Database(entities = [SampleEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao
}

// Function to serve as a factory or similar if needed, or expected by DI
// But for now, just the class is enough for the module to reference it (if we mock it).
// actually we need a 'createDatabase' function expectation for the DI module.
// In KMP with Room, you typically have a platform specific builder.
// We'll define a constructive expect/actual or just use the platform builder.
// For this 'core' module, I'll essentially define the class.
// The DI module in the prompt shows `single { createDatabase(get()) }`
// This implies `createDatabase` is a function we need to define, possibly in `platform` code.
