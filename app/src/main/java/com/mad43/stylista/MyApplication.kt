package com.mad43.stylista

import android.app.Application
import android.util.Log
import com.mad43.stylista.data.repo.currency.CurrencyRepoImp
import com.mad43.stylista.data.sharedPreferences.CurrencySharedPref
import com.mad43.stylista.data.sharedPreferences.CustomerManager
import com.mad43.stylista.data.sharedPreferences.SharedPref
import com.mad43.stylista.data.sharedPreferences.currency.CurrencyManager
import com.mad43.stylista.domain.remote.currency.GetCurrencyRateUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MyApplication : Application() {


    val context = SupervisorJob() + Dispatchers.Main
    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
        CurrencySharedPref.context = this
    }


}