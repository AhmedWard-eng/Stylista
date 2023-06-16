package com.mad43.stylista.ui.productInfo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.sharedPreferences.SharedPref.context
import com.mad43.stylista.databinding.ProductItemBinding
import com.mad43.stylista.domain.model.Review
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.util.setImageFromUrl

class AdapterFavourite (private var favouriteList: List<Favourite>,private val onClick:OnItemProductClicked) :
    RecyclerView.Adapter<AdapterFavourite.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(ProductItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = favouriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favouriteList[position])
    }

    fun setData(value: List<Favourite>){
        this.favouriteList = value
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favourite: Favourite) {
            binding.nameProduct.text = favourite.title
            binding.priceProduct.text = favourite.price
            binding.imageProduct.setImageFromUrl(favourite.image)
        }
    }
}