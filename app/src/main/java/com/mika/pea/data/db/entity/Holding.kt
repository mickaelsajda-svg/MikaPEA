package com.mika.pea.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holdings")
data class Holding(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String,
    val shares: Double,
    val avgPrice: Double,
    val currency: String = "EUR"
)
