package com.mad43.stylista.ui.oder.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.data.remote.entity.orders.LineItems
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.databinding.OrderDetailsItemBinding
import com.mad43.stylista.ui.orders.OnItemOrderClicked
import com.mad43.stylista.util.setImageFromUrl
import com.mad43.stylista.util.setPrice
import com.mad43.stylista.util.title

class OrderDetailsAdapter(
    private val onItemProductOrderClicked: OnItemProductOrderClicked) :
    ListAdapter<LineItems, OrderDetailsAdapter.ViewHolder>(DiffUtilsBrand()) {

    private lateinit var binding: OrderDetailsItemBinding

    inner class ViewHolder(var binding: OrderDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = OrderDetailsItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (! getItem(position).properties.isNullOrEmpty()){
            holder.binding.productDetailsImage.setImageFromUrl(
                getItem(position).properties?.get(0)?.value ?: ""
            )
        }

        holder.binding.productTitleValue.title(getItem(position).name.toString())

        holder.binding.priceValue.setPrice((getItem(position).price ?: "0.00").toDouble())

        holder.binding.quantityValue.text = getItem(position).quantity.toString()

        holder.binding.productOrderCard.setOnClickListener {
            onItemProductOrderClicked.productOrderClicked(getItem(position).product_id ?: 0)
        }
    }

}

class DiffUtilsBrand() : DiffUtil.ItemCallback<LineItems>() {

    override fun areItemsTheSame(oldItem: LineItems, newItem: LineItems): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: LineItems, newItem: LineItems): Boolean {
        return oldItem == newItem
    }

}

interface OnItemProductOrderClicked {
    fun productOrderClicked(id: Long)
}