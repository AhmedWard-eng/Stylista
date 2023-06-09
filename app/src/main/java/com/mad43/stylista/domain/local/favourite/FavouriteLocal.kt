package com.mad43.stylista.domain.local.favourite


import android.util.Log
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.DraftOrder
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPuttingRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.repo.auth.AuthRepository
import com.mad43.stylista.data.repo.auth.AuthRepositoryImp
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepo
import com.mad43.stylista.data.repo.favourite.FavouriteRepository
import com.mad43.stylista.data.repo.favourite.FavouriteRepositoryImp
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.domain.model.toCartItemList
import com.mad43.stylista.domain.remote.cart.toListOfPutLineItems
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.Flow

class FavouriteLocal (val favouriteRepositoryLocal: FavouriteLocalRepo , val favouriteRepositoryRemote : FavouriteRepository = FavouriteRepositoryImp()
) {

    suspend fun getStoredProduct(): Flow<List<Favourite>>{
        return favouriteRepositoryLocal.getStoredProduct()
    }
    suspend fun insertProduct(product: Favourite){
        favouriteRepositoryLocal.insertProduct(product)
    }
    suspend fun deleteProduct(product: Favourite){
        favouriteRepositoryLocal.deleteProduct(product)
    }

    fun isProductFavorite(productId: Long): Flow<Boolean> {
        return favouriteRepositoryLocal.isProductFavorite(productId)
    }

    fun deleteAllProducts() {
        favouriteRepositoryLocal.deleteAllProducts()
    }

    suspend fun insertFavouriteForCustumer(id: Long , draftOrderPutBody: DraftOrderPutBody): RemoteStatus<DraftOrderResponse> {
      return favouriteRepositoryRemote.insertFavouriteForCustomer(idFavourite = id , draftOrderPutBody)
    }

    fun getIDFavouriteForCustumer() : Result<LocalCustomer>{
       return favouriteRepositoryLocal.getCustomerData()
    }

    suspend fun getFavouriteUsingId(idFavourite: String): RemoteStatus<CustomDraftOrderResponse>{
        Log.e("Favourite Domain Layer", "getFavouriteUsingId: ${favouriteRepositoryRemote.getFavouriteUsingId(idFavourite)} ", )
        return favouriteRepositoryRemote.getFavouriteUsingId(id = idFavourite )
    }

     fun removeAnItemFromFavourite(putLineItems: MutableList<InsertingLineItem>, variantId: Long): List<InsertingLineItem> {
        val theUpdatingProduct = putLineItems.find {it.variant_id == variantId }

        if (theUpdatingProduct != null) {
            putLineItems.remove(theUpdatingProduct)
        }

        return putLineItems.toList()
    }

}