package com.mad43.stylista.ui.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.databinding.OrdersItemBinding
import com.mad43.stylista.util.setPrice

class OrdersAdapter(
    private val onItemOrderClicked: OnItemOrderClicked
) : ListAdapter<Orders, OrdersAdapter.ViewHolder>(DiffUtilsBrand()) {

    private lateinit var binding: OrdersItemBinding

    inner class ViewHolder(var binding: OrdersItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = OrdersItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.orderNumberValue.text = getItem(position).number.toString()

        holder.binding.priceValue.setPrice((getItem(position).current_subtotal_price ?: "0.00").toDouble())

        holder.binding.orderCard.setOnClickListener {
            onItemOrderClicked.orderClicked(getItem(position))
        }
    }

}

class DiffUtilsBrand() : DiffUtil.ItemCallback<Orders>() {

    override fun areItemsTheSame(oldItem: Orders, newItem: Orders): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Orders, newItem: Orders): Boolean {
        return oldItem == newItem
    }

}

interface OnItemOrderClicked {
    fun orderClicked(orders: Orders)
}
