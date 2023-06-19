package com.mad43.stylista.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.orders.DiscountCode
import com.mad43.stylista.data.remote.entity.orders.LineItems
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import com.mad43.stylista.data.repo.order.OrdersRepo
import com.mad43.stylista.data.sharedPreferences.currency.CurrencyManager
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authUseCase: AuthUseCase = AuthUseCase(),
    private val currencyManager: CurrencyManager = CurrencyManager(),
    private val ordersRepo: OrdersRepo = OrdersRepo(),
) : ViewModel() {

    private var _loginState: MutableStateFlow<RemoteStatus<LoginResponse>> = MutableStateFlow(
        RemoteStatus.Loading
    )
    var loginState: StateFlow<RemoteStatus<LoginResponse>> = _loginState

    var orders = MutableStateFlow<RemoteStatus<List<Orders>>>(RemoteStatus.Loading)

    init {
        getOrders()
        postOrder()
    }

    fun getUserName(): String {
        var userData = authUseCase.getCustomerData()
        var userName = userData.getOrNull()?.userName
        if (userName == null) {
            return "guest"
        } else {
            return userName.toString()
        }
    }

    fun getCurrencyCode(): String {
        return currencyManager.getCurrencyPair().first
    }

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout()
        }
    }

    private fun getOrders() {
        viewModelScope.launch {
            try {
                ordersRepo.getAllOrders().catch { e ->
                    orders.value = RemoteStatus.Failure(e)
                }.collect { data ->
                    orders.value = RemoteStatus.Success(data)
                }
            } catch (e: Exception) {
                orders.value = RemoteStatus.Failure(e)
            }

        }
    }

    private fun postOrder() {
        val item1 = LineItems(price = "100", variantId = 45434770194731, quantity = 1)
        val item2 = LineItems(price = "500", variantId = 45434770358571, quantity = 5)
        val items: List<LineItems> = listOf(item1, item2)


        val dis1 = DiscountCode("100", "100", "1")
        val dis2 = DiscountCode("500", "500", "5")
        val dis3 = DiscountCode("700", "700", "7")
        val dis: List<DiscountCode> = listOf(dis1, dis2, dis3)

        val order = Orders(discount_codes = dis,
           email =  "hfatma791@gmail.com", lineItems =  items
        )

        val orderPost = PostOrderResponse(order)

        viewModelScope.launch {
            try {
                ordersRepo.postOrder(orderPost)
            }catch (e:Exception){
                Log.i("OrdersPost",e.message.toString())
            }
        }
    }

}