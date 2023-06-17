package com.mad43.stylista.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.domain.remote.cart.DeleteCartItemUseCase
import com.mad43.stylista.domain.remote.cart.EditCartItemUseCase
import com.mad43.stylista.domain.remote.cart.GetCartListUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.sql.RowId

class CartViewModel(
    private val edit: EditCartItemUseCase = EditCartItemUseCase(),
    private val deleteCartItemUseCase: DeleteCartItemUseCase = DeleteCartItemUseCase(),
    private val getCartListUseCase: GetCartListUseCase = GetCartListUseCase()
) : ViewModel() {

    var list : List<CartItem> = listOf()
    private val _productListStatus = MutableStateFlow<RemoteStatus<List<CartItem>>>(RemoteStatus.Loading)
    val productListStatus : StateFlow<RemoteStatus<List<CartItem>>> = _productListStatus.asStateFlow()

    val deleteCommand = MutableStateFlow<Action>(Action.Nothing)

    val editCommand = MutableStateFlow<Action>(Action.Nothing)


    init {
        getListOfCartItem()

        viewModelScope.launch {
            deleteCommand.collect{
                if (it is Action.Delete){
                    removeCartItem(it.variantId)
                }
            }
        }

        viewModelScope.launch {
            editCommand.collect{
                if (it is Action.Edit){
                    editQuantityOfItem(it.variantId,it.newQuantity,it.cartItems)
                }
            }
        }
    }
    private fun editQuantityOfItem(variantId : Long,newQuantity: Int,cartItems : List<CartItem>){
        viewModelScope.launch {
            _productListStatus.emit(edit(variantId = variantId, quantity = newQuantity, cartId = 1125545345323,cartItems))
        }
    }
    private fun getListOfCartItem(){
        viewModelScope.launch {
            _productListStatus.emit(getCartListUseCase(cartId = "1125545345323"))
        }
    }
    private fun removeCartItem(variantId : Long){
        viewModelScope.launch {
            _productListStatus.emit(deleteCartItemUseCase(variantId = variantId,cartId = 1125545345323))
        }
    }

    fun getTotalPrice() : Double{
        return list.sumOf {
            it.price * it.quantity
        }
    }

}
sealed class Action{
    data class Delete(val variantId: Long) : Action()
    object Nothing : Action()
    data class Edit(val variantId: Long,val newQuantity: Int,val cartItems : List<CartItem>) : Action()
}