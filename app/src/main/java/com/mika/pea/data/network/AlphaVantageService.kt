package com.mika.pea.data.network

import retrofit2.http.GET
import retrofit2.http.Query

data class GlobalQuote(
    val `Global Quote`: Map<String, String>?
)

interface AlphaVantageService {
    @GET("query?function=GLOBAL_QUOTE")
    suspend fun quote(
        @Query("symbol") symbol: String,
        @Query("apikey") apikey: String
    ): GlobalQuote
}
