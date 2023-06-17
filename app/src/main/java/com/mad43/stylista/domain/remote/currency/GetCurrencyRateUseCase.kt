package com.mad43.stylista.domain.remote.currency

import com.mad43.stylista.data.repo.currency.CurrencyRepo
import com.mad43.stylista.data.repo.currency.CurrencyRepoImp
import com.mad43.stylista.util.RemoteStatus
import java.lang.Exception

class GetCurrencyRateUseCase(private val currencyRepo: CurrencyRepo = CurrencyRepoImp()) {

    suspend operator fun invoke(currencyCode: String): RemoteStatus<Pair<Double,String>> {
        return try {
            val rate = currencyRepo.getCurrencyRate().rates[currencyCode]
            if (rate != null)
                RemoteStatus.Success(Pair(rate,currencyCode))
            else {
                RemoteStatus.Failure(Exception("rate is null"))
            }
        } catch (e: Exception) {
            RemoteStatus.Failure(e)
        }

    }
}