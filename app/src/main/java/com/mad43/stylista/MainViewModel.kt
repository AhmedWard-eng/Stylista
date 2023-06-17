package com.mad43.stylista

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.repo.currency.CurrencyRepo
import com.mad43.stylista.data.repo.currency.CurrencyRepoImp
import com.mad43.stylista.data.sharedPreferences.currency.CurrencyManager
import kotlinx.coroutines.launch

class MainViewModel(
    private val currencyManager: CurrencyManager = CurrencyManager(),
    private val currencyRepo: CurrencyRepo = CurrencyRepoImp()): ViewModel() {

    fun updateCurrencyRate(){
        viewModelScope.launch {
            try {
                val pair = currencyManager.getCurrencyPair()
                val rate = currencyRepo.getCurrencyRate().rates[pair.first]
                Log.d("TAG", "updateCurrencyRate: $rate")
                if(rate != null){
                    currencyManager.setCurrency(pair.copy(second = rate))
                }
            }catch (e:Exception){
                Log.e("TAG", "updateCurrencyRate: ",e)
            }
        }
    }

}