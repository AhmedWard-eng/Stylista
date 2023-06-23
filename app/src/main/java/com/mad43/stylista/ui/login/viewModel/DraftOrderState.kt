package com.mad43.stylista.ui.login.viewModel

import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.DraftOrder

sealed class DraftOrderState {
    open class OnSuccess(var draftOrder: DraftOrder) : DraftOrderState()

    class OnFail(val errorMessage: Throwable) : DraftOrderState()
    object Loading : DraftOrderState()
}