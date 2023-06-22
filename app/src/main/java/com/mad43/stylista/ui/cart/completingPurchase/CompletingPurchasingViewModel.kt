package com.mad43.stylista.ui.cart.completingPurchase

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.remote.entity.orders.DiscountCode
import com.mad43.stylista.data.remote.entity.orders.LineItems
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import com.mad43.stylista.data.repo.order.OrdersRepo
import com.mad43.stylista.data.repo.order.OrdersRepoInterface
import com.mad43.stylista.data.sharedPreferences.CustomerManager
import com.mad43.stylista.data.sharedPreferences.PreferencesData
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.domain.model.CouponItem
import com.mad43.stylista.domain.remote.GetCouponsListUseCase
import com.mad43.stylista.domain.remote.address.GetDefaultAddressUseCase
import com.mad43.stylista.domain.remote.cart.ClearCartUseCase
import com.mad43.stylista.domain.remote.postingOrder.PostingOrderUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class CompletingPurchasingViewModel(
    private val getDefaultAddressUseCase: GetDefaultAddressUseCase = GetDefaultAddressUseCase(),
    private val customerManager: CustomerManager = PreferencesData(),
    private val getCouponsListUseCase: GetCouponsListUseCase = GetCouponsListUseCase(),
    private val postingOrderUseCase: PostingOrderUseCase = PostingOrderUseCase(),
    private val clearCartUseCase: ClearCartUseCase = ClearCartUseCase()
) : ViewModel() {

    private val _defaultAddressState =
        MutableStateFlow<RemoteStatus<AddressItem>>(RemoteStatus.Loading)
    val defaultAddressState = _defaultAddressState.asStateFlow()

    private val _validateCouponStatus =
        MutableStateFlow<RemoteStatus<CouponItem>>(RemoteStatus.Loading)
    val validateCouponStatus = _validateCouponStatus.asStateFlow()

    private val _postingOrderState = MutableStateFlow<RemoteStatus<Boolean>>(RemoteStatus.Loading)
    val postingOrderState = _postingOrderState.asStateFlow()

    var paymentType = PaymentType.GOOGLE_PAY
    var cartList: List<CartItem> = listOf()
    var address: AddressItem? = null
    var discountAmount: Double = -0.0
    var discountType: String = ""
    var goingToAddNewAddress = false
    var discountCode = ""
    var userId: Long? = null
    var email: String? =  null
    var cartId : Long? = null

    init {
        if (customerManager.getCustomerData().isSuccess){
            userId = customerManager.getCustomerData().getOrNull()?.customerId
            cartId = customerManager.getCustomerData().getOrNull()?.cardID?.toLong()
            email = customerManager.getCustomerData().getOrNull()?.email
        }

        getDefaultAddress()
    }


    fun clearCart(){
        viewModelScope.launch {
            clearCartUseCase(cartId = cartId ?: 0)
        }
    }

    fun isUserAuth(): Boolean {
        return customerManager.getCustomerData().isSuccess
    }

    fun getDefaultAddress() {
        viewModelScope.launch {
            _defaultAddressState.emit(getDefaultAddressUseCase(customerId = userId.toString()))
        }
    }

    fun getOrderTotalPrice(): Double {
        return cartList.sumOf { it.price * it.quantity }
    }

    fun applyCoupon(coupon: String) {
        viewModelScope.launch {
            val status = getCouponsListUseCase()
            if (status is RemoteStatus.Success) {
                val myCoupon = status.data.find { it.code == coupon }
                if (myCoupon == null) {
                    _validateCouponStatus.emit(RemoteStatus.Failure(NotExistedException()))
                } else {
                    if (myCoupon.isNotExpired) {
                        _validateCouponStatus.emit(RemoteStatus.Success(myCoupon))
                    } else {
                        _validateCouponStatus.emit(RemoteStatus.Failure(CouponExpiredException()))
                    }
                }
            } else if (status is RemoteStatus.Failure) {
                _validateCouponStatus.emit(RemoteStatus.Failure(status.msg))
            }
        }
    }


    fun getDiscount(): Double {
        return if (discountType == "fixed_amount") {
            if (getOrderTotalPrice() >= 2 * discountAmount) {
                discountAmount
            } else {
                _validateCouponStatus.value = RemoteStatus.Failure(CantApplyDiscountException())
                0.0
            }
        } else if (discountType == "percentage") {
            getOrderTotalPrice() * discountAmount
        } else {
            0.0
        }
    }

    fun postOrder(orderPost : PostOrderResponse) {
        viewModelScope.launch {
            _postingOrderState.emit(postingOrderUseCase(orderPost))
        }
    }
}

enum class PaymentType {
    COD,
    GOOGLE_PAY
}

class CouponExpiredException : Exception()
class NotExistedException : Exception()

class CantApplyDiscountException : Exception()