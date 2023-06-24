package com.mad43.stylista.ui.profile.view

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.databinding.RowFavouriteBinding
import com.mad43.stylista.databinding.RowWishListBinding
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.util.NetwarkInternet
import com.mad43.stylista.util.setImageFromUrl

class AdapterWishList (private var favouriteList: List<Favourite>, private val onClick: OnItemProductClicked) :
    RecyclerView.Adapter<AdapterWishList.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        return ViewHolder(RowWishListBinding.inflate(inflater, parent, false),onClick)
    }

    override fun getItemCount(): Int = favouriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favouriteList[position])
    }

    fun setData(value: List<Favourite>){
        this.favouriteList = value
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: RowWishListBinding, private val listener: OnItemProductClicked): RecyclerView.ViewHolder(binding.root) {
        fun bind(favourite: Favourite) {
            binding.nameProduct.text = favourite.title
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