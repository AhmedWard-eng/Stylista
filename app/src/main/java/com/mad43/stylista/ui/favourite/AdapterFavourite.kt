package com.mad43.stylista.ui.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.databinding.ProductItemBinding
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.util.setImageFromUrl

class AdapterFavourite (private var favouriteList: List<Favourite>,private val onClick:OnItemProductClicked) :
    RecyclerView.Adapter<AdapterFavourite.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(ProductItemBinding.inflate(inflater, parent, false),onClick)
    }

    override fun getItemCount(): Int = favouriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favouriteList[position])
    }

    fun setData(value: List<Favourite>){
        this.favouriteList = value
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ProductItemBinding, private val listener: OnItemProductClicked): RecyclerView.ViewHolder(binding.root) {
        fun bind(favourite: Favourite) {
            binding.nameProduct.text = favourite.title
            binding.priceProduct.text = favourite.price
            binding.imageProduct.setImageFromUrl(favourite.image)
            binding.cardProduct.setOnClickListener {
                listener.productClicked(favourite.id)
            }
        }
    }
}

