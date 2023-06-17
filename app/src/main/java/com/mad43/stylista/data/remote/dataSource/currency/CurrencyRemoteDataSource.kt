package com.mad43.stylista.data.remote.dataSource.currency

import com.mad43.stylista.data.remote.entity.currency.CurrencyRate

interface CurrencyRemoteDataSource {
    suspend fun getCurrencyRate(): CurrencyRate
}