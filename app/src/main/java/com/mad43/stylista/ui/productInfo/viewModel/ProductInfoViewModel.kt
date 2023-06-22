package com.mad43.stylista.ui.productInfo.viewModel

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPuttingRequestBody
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.model.PuttingCartItem
import com.mad43.stylista.domain.remote.cart.PutItemInCartUseCase
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductInfoViewModel(
    private val productInfo: ProductInfo = ProductInfo(),
    private val favourite: FavouriteLocal,
    private val putItemInCartUseCase: PutItemInCartUseCase = PutItemInCartUseCase(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApiState>(ApiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiStateNetwork =
        MutableStateFlow<RemoteStatus<CustomDraftOrderResponse>>(RemoteStatus.Loading)
    val uiStateNetwork: StateFlow<RemoteStatus<CustomDraftOrderResponse>> = _uiStateNetwork

    val imagesArray = ArrayList<SlideModel>()

    private val _favourite = MutableStateFlow<RemoteStatus<List<Favourite>>>(RemoteStatus.Loading)
    val favouriteList = _favourite.asStateFlow()

    private val _isfavourite = MutableStateFlow<RemoteStatus<Boolean>>(RemoteStatus.Loading)
    val isfavouriteExist = _isfavourite.asStateFlow()

    private val _addedToCart = MutableStateFlow<RemoteStatus<Boolean>?>(RemoteStatus.Loading)
    val addedToCart = _addedToCart.asStateFlow()

    var customDraftOrderList = mutableListOf<CustomDraftOrderResponse>()
    var lineItemsList = mutableListOf<InsertingLineItem>()
    lateinit var requestBody: DraftOrderPuttingRequestBody
    lateinit var lineItem1: InsertingLineItem
    lateinit var favID: String


    var availableSizesTitle = mutableSetOf<String>()
    var availableSizesID = mutableSetOf<Long>()
    lateinit var sizeIdPairs: List<Pair<String, Long>>
    var selectedSize: String = ""
    var idVariansSelect: Long? = null

    private val _userExists = MutableStateFlow(false)
    val userExists: StateFlow<Boolean> = _userExists.asStateFlow()
    fun getProductDetails(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            productInfo.getProductDetails(id).catch { e -> _uiState.value = ApiState.Failure(e) }
                .collect { data ->
                    _uiState.value = ApiState.Success(data)
                    for (i in 0..data.product.images.size - 1) {
                        var imag = data.product.images.get(i).src
                        imagesArray.add(SlideModel(imag))
                    }

                }
        }
    }
    fun resetAddingToCart(){
        _addedToCart.value = null
    }

//    fun insertItemInCart(){
//        putItemInCartUseCase(PuttingCartItem())
//    }

    fun insertProduct(product: Favourite) {
        viewModelScope.launch(Dispatchers.IO) {
            favourite.insertProduct(product)
        }
    }

    fun deleteProduct(product: Favourite) {
        viewModelScope.launch(Dispatchers.IO) {
            favourite.deleteProduct(product)
        }
    }

    fun getLocalFavourite() {
        viewModelScope.launch(Dispatchers.IO) {
            favourite.getStoredProduct()
                .catch { e ->
                    _favourite.value = RemoteStatus.Failure(e)
                    Log.i(ContentValues.TAG, "getLocalFavourite: FailureFailureFailureFailure")
                }
                .collect { data ->
                    _favourite.value = RemoteStatus.Success(data)
                }
        }
    }

    fun isFavourite(productID: Long) {
        viewModelScope.launch {
            favourite.isProductFavorite(productID).catch { e ->
                _isfavourite.value = RemoteStatus.Failure(e)
            }.collectLatest { data ->
                _isfavourite.value = RemoteStatus.Success(data)
            }
        }
    }

    fun insertFavouriteForCustumer(id: Long, draftOrderPutBody: DraftOrderPutBody) {
        viewModelScope.launch(Dispatchers.IO) {
            favourite.insertFavouriteForCustumer(id = id, draftOrderPutBody)

        }

    }

    suspend fun getLineItems(idFav: String): MutableList<CustomDraftOrderResponse> {
        var getProduct = favourite.getFavouriteUsingId(idFav)

        return when (getProduct) {
            is RemoteStatus.Success -> mutableListOf(getProduct.data)
            else -> mutableListOf()
        }
    }

    fun getIDForFavourite(): Long {
        val customerData = favourite.getIDFavouriteForCustumer()

        return try {
            if (customerData.isSuccess) {
                val localCustomer = customerData.getOrNull()
                val favouriteId = localCustomer?.favouriteID
                if (favouriteId != null) {
                    favouriteId.toLong()
                } else {
                    throw Exception("Favourite ID not found")
                }
            } else {
                throw Exception("Customer data not found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in getIDForFavourite(): ${e.message}")
            -1L
        }
    }

    fun getFavouriteUsingId(idFavourite: String) {
        viewModelScope.launch {
            try {
                val customDraftOrderResponse = favourite.getFavouriteUsingId(idFavourite)
                _uiStateNetwork.value = customDraftOrderResponse
                Log.d(TAG, "getFavouriteUsingId: ${_uiStateNetwork.value}")
            } catch (e: Exception) {
                _uiStateNetwork.value = RemoteStatus.Failure(e)
            }
        }
    }

    fun removeAnItemFromFavourite(
        putLineItems: MutableList<InsertingLineItem>,
        variantId: Long
    ): List<InsertingLineItem> {
        return favourite.removeAnItemFromFavourite(putLineItems, variantId)
    }

    fun insertAllProductToList() {
        for (customDraftOrder in customDraftOrderList) {
            for (lineItem in customDraftOrder.draft_order?.line_items.orEmpty()) {
                var titleOld = lineItem.title
                var priceOld = lineItem.price
                var varianceIDOld = lineItem.variant_id
                var imageOld = lineItem.properties
                Log.d(
                    TAG, "////customDraftOrderList: ${customDraftOrderList.size}" +
                            " ${lineItem.title} "
                )
                var lineItem2 = InsertingLineItem(
                    properties = imageOld,
                    variant_id = varianceIDOld,
                    quantity = 1,
                    price = priceOld,
                    title = titleOld
                )
                lineItemsList.add(lineItem2)
            }
        }
    }

    fun insertProductToList(lineItem1: InsertingLineItem) {
        insertAllProductToList()
        lineItemsList.add(lineItem1)
        requestBody = DraftOrderPuttingRequestBody(
            line_items = lineItemsList
        )
        insertFavouriteForCustumer(favID.toLong(), DraftOrderPutBody(requestBody))
    }

    fun removeProductFromFavourite() {
        insertAllProductToList()
        var newList = lineItem1.variant_id?.let { item ->
            removeAnItemFromFavourite(
                lineItemsList,
                item
            )
        }
        requestBody = newList?.let { it1 ->
            DraftOrderPuttingRequestBody(
                line_items = it1
            )
        }!!
        insertFavouriteForCustumer(favID.toLong(), DraftOrderPutBody(requestBody))
    }

    fun checkUserIsLogin() {
        val customerData = favourite.getIDFavouriteForCustumer()
        _userExists.value = customerData.isSuccess
    }

    private fun getCustomerData(): LocalCustomer {

        val customerData = favourite.getIDFavouriteForCustumer()
        if (customerData.isSuccess) {
            val data = customerData.getOrNull()
            if (data != null) {
                return data
            } else {
                throw Exception()
            }
        } else {
            throw Exception()
        }
    }

    fun putItemInCart(variantId: Long, imageUrl: String) {

        viewModelScope.launch {
            try {
                val status = putItemInCartUseCase(
                    PuttingCartItem(
                        cartId = getCustomerData().cardID.toLong(),
                        variantId,
                        quantity = 1,
                        userEmail = getCustomerData().email,
                        imageUrl = imageUrl
                    )
                )
                if (status is RemoteStatus.Success) {
                    _addedToCart.emit(RemoteStatus.Success(true))
                } else if (status is RemoteStatus.Failure) {
                    _addedToCart.emit(status)
                }
            } catch (e: Exception) {
                _addedToCart.emit(RemoteStatus.Failure(e))
            }
        }


    }
}