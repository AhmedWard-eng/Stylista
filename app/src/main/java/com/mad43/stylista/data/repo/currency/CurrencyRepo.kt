package com.mad43.stylista.data.repo.currency

import com.mad43.stylista.data.remote.entity.currency.CurrencyRate

interface CurrencyRepo {

    suspend fun getCurrencyRate() : CurrencyRate
}