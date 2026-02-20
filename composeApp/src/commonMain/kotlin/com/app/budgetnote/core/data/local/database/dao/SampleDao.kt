package core.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.data.local.database.entity.SampleEntity

@Dao
interface SampleDao {
    @Query("SELECT * FROM sample")
    suspend fun getAll(): List<SampleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SampleEntity)

    @Query("DELETE FROM sample WHERE sampleId = :id")
    suspend fun deleteById(id: String)
}
