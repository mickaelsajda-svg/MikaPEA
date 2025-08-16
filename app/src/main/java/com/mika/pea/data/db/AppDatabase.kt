package com.mika.pea.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mika.pea.data.db.dao.HoldingDao
import com.mika.pea.data.db.entity.Holding

@Database(entities = [Holding::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holdingDao(): HoldingDao
}
