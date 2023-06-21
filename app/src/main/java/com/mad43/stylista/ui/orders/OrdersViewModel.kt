package com.mad43.stylista.ui.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.data.repo.order.OrdersRepo
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.lang.Exception

class OrdersViewModel(
    private val ordersRepo: OrdersRepo = OrdersRepo(),
) : ViewModel() {

    var orders = MutableStateFlow<RemoteStatus<List<Orders>>>(RemoteStatus.Loading)

    init {
        getOrders()
    }

    private fun getOrders() {
        viewModelScope.launch {
            Log.i("DDDDDD","Enterrrrrrrr")
            try {
                Log.i("DDDDDD","Enter")
                ordersRepo.getAllOrders().catch { e ->
                    Log.i("DDDDDD","EnterF")
                    orders.value = RemoteStatus.Failure(e)
                }.collect { data ->
                    Log.i("DDDDDD","EnterS")
                    orders.value = RemoteStatus.Success(data)
                }
            } catch (e: Exception) {
                Log.i("DDDDDD","EnterFF")
                Log.i("DDDDDD",e.message.toString())
                orders.value = RemoteStatus.Failure(e)
            }

        }
    }

}