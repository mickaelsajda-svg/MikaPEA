package com.mika.pea.data.db.dao

import androidx.room.*
import com.mika.pea.data.db.entity.Holding
import kotlinx.coroutines.flow.Flow

@Dao
interface HoldingDao {
    @Query("SELECT * FROM holdings ORDER BY symbol ASC")
    fun all(): Flow<List<Holding>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(h: Holding)

    @Update
    suspend fun update(h: Holding)

    @Delete
    suspend fun delete(h: Holding)
}
