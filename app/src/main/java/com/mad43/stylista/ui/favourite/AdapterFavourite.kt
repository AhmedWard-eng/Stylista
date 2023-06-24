package com.mad43.stylista.ui.favourite

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.databinding.ProductItemBinding
import com.mad43.stylista.databinding.RowFavouriteBinding
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.util.NetwarkInternet
import com.mad43.stylista.util.setImageFromUrl
import com.mad43.stylista.util.title

class AdapterFavourite (private var favouriteList: List<Favourite>,private val onClick:OnItemProductClicked) :
    RecyclerView.Adapter<AdapterFavourite.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(RowFavouriteBinding.inflate(inflater, parent, false),onClick)
    }

    override fun getItemCount(): Int = favouriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favouriteList[position])
    }

    fun setData(value: List<Favourite>){
        this.favouriteList = value
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: RowFavouriteBinding, private val listener: OnItemProductClicked): RecyclerView.ViewHolder(binding.root) {
        fun bind(favourite: Favourite) {
            binding.nameProduct.title(favourite.title)
            binding.priceProduct.text = favourite.price
            binding.imageProduct.setImageFromUrl(favourite.image)
            val context = itemView.context
            binding.cardProduct.setOnClickListener {
                if (NetwarkInternet().isNetworkAvailable(context = context)){
                    listener.productClicked(favourite.id)
                }else{
                    NetwarkInternet().displayNetworkDialog(context)
                }
            }
        }
    }
}

