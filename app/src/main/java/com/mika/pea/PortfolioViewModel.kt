package com.mika.pea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mika.pea.data.prefs.PreferencesRepository
import com.mika.pea.data.AppContainer
import com.mika.pea.data.db.entity.Holding
import com.mika.pea.domain.PortfolioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UiHolding(
    val holding: Holding,
    val lastPrice: Double? = null
) {
    val pnlValue: Double?
        get() = lastPrice?.let { (it - holding.avgPrice) * holding.shares }
    val marketValue: Double?
        get() = lastPrice?.let { it * holding.shares }
}

data class UiState(
    val items: List<UiHolding> = emptyList(),
    val apiKey: String = "",
    val message: String? = null,
    val isRefreshing: Boolean = false
)

class PortfolioViewModel(
    private val repo: PortfolioRepository,
    private val prefs: PreferencesRepository
) : ViewModel() {

    private val _prices = MutableStateFlow<Map<String, Double>>(emptyMap())

    val state: StateFlow<UiState> = combine(
        repo.holdings(),
        _prices,
        prefs.apiKey
    ) { holdings, prices, key ->
        UiState(
            items = holdings.map { h -> UiHolding(h, prices[h.symbol]) },
            apiKey = key
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    fun add(symbol: String, shares: Double, avgPrice: Double, currency: String) {
        viewModelScope.launch {
            repo.add(symbol, shares, avgPrice, currency)
        }
    }

    fun delete(h: Holding) {
        viewModelScope.launch { repo.delete(h) }
    }

    fun setApiKey(key: String) {
        viewModelScope.launch { prefs.setApiKey(key) }
    }

    fun refreshPrice(symbol: String) {
        viewModelScope.launch {
            val p = repo.fetchPrice(symbol)
            if (p != null) {
                _prices.update { it + (symbol to p) }
            }
        }
    }

    fun refreshAll() {
        viewModelScope.launch {
            state.value.items.forEach { refreshPrice(it.holding.symbol) }
        }
    }

    class Factory(private val container: AppContainer) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PortfolioViewModel(container.repository, container.prefs) as T
        }
    }
}
