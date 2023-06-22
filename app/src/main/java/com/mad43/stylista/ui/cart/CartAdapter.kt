package com.mad43.stylista.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.R
import com.mad43.stylista.databinding.CartItemBinding
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.util.setImageFromUrl
import com.mad43.stylista.util.setPrice

class CartAdapter(private val clickListener: ClickListener) : ListAdapter<CartItem,CartAdapter.ViewHolder>(DiffUtils) {

    class ViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item.quantity < 2){
            holder.binding.buttonDecreaseQuantity.isEnabled = false
            holder.binding.buttonDecreaseQuantity.background = AppCompatResources.getDrawable(
                holder.binding.root.context,R.drawable.button_checkout_background_left_disabled)

        }else{
            holder.binding.buttonDecreaseQuantity.isEnabled = true
            holder.binding.buttonDecreaseQuantity.background = AppCompatResources.getDrawable(
                holder.binding.root.context,R.drawable.button_checkout_background_left)

        }
        holder.binding.cartProductImageView.setImageFromUrl(item.imageUrl)
        holder.binding.textViewProductTitle.text = item.title
        holder.binding.textViewProductPrice.setPrice(item.price)
        holder.binding.textViewQuantity.text = item.quantity.toString()


        holder.binding.buttonRemoveFromCart.setOnClickListener {
            clickListener.delete(item.variant_id)
        }

        holder.binding.buttonIncreaseQuantity.setOnClickListener {
            clickListener.setQuantity(item.variant_id,item.quantity + 1,true)
        }

        holder.binding.buttonDecreaseQuantity.setOnClickListener {

            clickListener.setQuantity(item.variant_id,item.quantity - 1,false)
        }
        holder.binding.textViewVariantTitle.text = item.variant_title
    }


    object DiffUtils : DiffUtil.ItemCallback<CartItem>(){
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return (oldItem.variant_id == newItem.variant_id)
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }

    class ClickListener(private val setQuantityListener : (Long,Int,Boolean) -> Unit ,private val deleteListener : (Long) -> Unit){
        fun setQuantity(variantId: Long , quantity: Int,isIncreasing : Boolean) = setQuantityListener(variantId,quantity,isIncreasing)

        fun delete(variantId: Long) = deleteListener(variantId)
    }


}


