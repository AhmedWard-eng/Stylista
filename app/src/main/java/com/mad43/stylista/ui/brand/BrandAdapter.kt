package com.mad43.stylista.ui.brand

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mad43.stylista.databinding.ProductItemBinding
import com.mad43.stylista.domain.model.DisplayProduct
import com.mad43.stylista.util.setPrice
import com.mad43.stylista.util.setImageFromUrl
import com.mad43.stylista.util.title


class BrandAdapter(
    private val onItemProductClicked: OnItemProductClicked
) : ListAdapter<DisplayProduct, BrandAdapter.ViewHolder>(DiffUtilsBrand()) {

    private lateinit var binding: ProductItemBinding

    inner class ViewHolder(var binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ProductItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.imageProduct.setImageFromUrl(getItem(position).image)

        holder.binding.nameProduct.title(getItem(position).title)

        holder.binding.priceProduct.setPrice(getItem(position).price.toDouble())

        holder.binding.cardProduct.setOnClickListener {
            onItemProductClicked.productClicked(getItem(position).id)
        }
    }

}

class DiffUtilsBrand() : DiffUtil.ItemCallback<DisplayProduct>() {

    override fun areItemsTheSame(oldItem: DisplayProduct, newItem: DisplayProduct): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DisplayProduct, newItem: DisplayProduct): Boolean {
        return oldItem == newItem
    }

}

interface OnItemProductClicked {
    fun productClicked(id: Long)
}
