package com.mad43.stylista.data.repo.currency

import com.mad43.stylista.data.remote.dataSource.currency.CurrencyRemoteDataSource
import com.mad43.stylista.data.remote.dataSource.currency.CurrencyRemoteDataSourceImp
import com.mad43.stylista.data.remote.entity.currency.CurrencyRate


class CurrencyRepoImp(private val currencyRemoteDataSource: CurrencyRemoteDataSource = CurrencyRemoteDataSourceImp()) : CurrencyRepo {
    override suspend fun getCurrencyRate(): CurrencyRate {
       return currencyRemoteDataSource.getCurrencyRate()
    }
}