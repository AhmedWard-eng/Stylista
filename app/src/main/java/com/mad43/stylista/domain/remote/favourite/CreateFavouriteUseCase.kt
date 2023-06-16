package com.mad43.stylista.domain.remote.favourite

import com.mad43.stylista.data.repo.favourite.FavouriteRepository
import com.mad43.stylista.data.repo.favourite.FavouriteRepositoryImp
import com.mad43.stylista.util.RemoteStatus

class CreateFavouriteUseCase (private val favouriteRepo: FavouriteRepository = FavouriteRepositoryImp()) {

    suspend operator fun invoke(customerId : Long) : RemoteStatus<Long> {
        return favouriteRepo.createFavouriteForCustomer(customerId)
    }
}