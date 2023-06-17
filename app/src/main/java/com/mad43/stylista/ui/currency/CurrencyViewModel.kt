package com.mad43.stylista.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.sharedPreferences.currency.CurrencyManager
import com.mad43.stylista.domain.remote.currency.GetCurrencyRateUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase = GetCurrencyRateUseCase(),
    private val currencyManager: CurrencyManager = CurrencyManager()
) : ViewModel() {

    private val _currencyRateState: MutableStateFlow<RemoteStatus<Pair<Double, String>>?> =
        MutableStateFlow(null)
    val currencyRateState = _currencyRateState.asStateFlow()
    fun getCurrencyRate(currencyCode: String) {
        _currencyRateState.value = RemoteStatus.Loading
        viewModelScope.launch {
            val state = getCurrencyRateUseCase(currencyCode)
            _currencyRateState.emit(state)
        }
    }

    fun setCurrencyInShared(currencyPair: Pair<String,Double>){
        currencyManager.setCurrency(currencyPair)
    }
}