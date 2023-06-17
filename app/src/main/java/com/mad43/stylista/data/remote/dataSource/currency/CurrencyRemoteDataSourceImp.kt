package com.mad43.stylista.data.remote.dataSource.currency

import com.mad43.stylista.data.remote.entity.currency.CurrencyRate
import com.mad43.stylista.data.remote.network.CurrencyRetrofitService
import com.mad43.stylista.data.remote.network.currency.CurrencyApiInterface

class CurrencyRemoteDataSourceImp(private val currencyApiInterface: CurrencyApiInterface = CurrencyRetrofitService.currencyApiInterface):CurrencyRemoteDataSource {
    override suspend fun getCurrencyRate(): CurrencyRate {
        return currencyApiInterface.getCurrenciesRate()
    }
}