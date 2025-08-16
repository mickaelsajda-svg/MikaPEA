package com.mika.pea.domain

import com.mika.pea.data.db.dao.HoldingDao
import com.mika.pea.data.db.entity.Holding
import com.mika.pea.data.network.AlphaVantageService
import com.mika.pea.data.prefs.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PortfolioRepository(
    private val dao: HoldingDao,
    private val alpha: AlphaVantageService,
    private val prefs: PreferencesRepository
) {
    fun holdings(): Flow<List<Holding>> = dao.all()

    suspend fun add(symbol: String, shares: Double, avgPrice: Double, currency: String) {
        dao.insert(Holding(symbol = symbol.uppercase(), shares = shares, avgPrice = avgPrice, currency = currency.uppercase()))
    }

    suspend fun update(h: Holding) = dao.update(h)
    suspend fun delete(h: Holding) = dao.delete(h)

    suspend fun fetchPrice(symbol: String): Double? {
        val key = prefs.apiKey.first()
        if (key.isBlank()) return null
        return try {
            val resp = alpha.quote(symbol, key)
            val priceStr = resp.`Global Quote`?.get("05. price")
            priceStr?.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
}
