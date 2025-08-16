package com.mika.pea.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.mika.pea.data.db.AppDatabase
import com.mika.pea.data.network.AlphaVantageService
import com.mika.pea.data.prefs.PreferencesRepository
import com.mika.pea.domain.PortfolioRepository
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val Context.dataStore by preferencesDataStore(name = "settings")

interface AppContainer {
    val repository: PortfolioRepository
    val prefs: PreferencesRepository
}

class AppContainerImpl(context: Context) : AppContainer {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "portfolio.db"
    ).fallbackToDestructiveMigration().build()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val moshi = Moshi.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.alphavantage.co/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    private val alpha = retrofit.create(AlphaVantageService::class.java)

    override val prefs: PreferencesRepository = PreferencesRepository(context.dataStore)
    override val repository: PortfolioRepository = PortfolioRepository(db.holdingDao(), alpha, prefs)
}
