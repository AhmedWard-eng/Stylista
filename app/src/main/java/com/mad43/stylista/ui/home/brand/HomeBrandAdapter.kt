package com.mad43.stylista.ui.home.brand

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mad43.stylista.databinding.BrandItemBinding
import com.mad43.stylista.domain.model.DisplayBrand

class HomeBrandAdapter(
private val context: Context
) : ListAdapter<DisplayBrand, HomeBrandAdapter.ViewHolder>(DiffUtilsHomeBrand()){

    private lateinit var binding : BrandItemBinding

    inner class ViewHolder(var binding: BrandItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = BrandItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(getItem(position).image)
            .apply(
                RequestOptions().override(
                    holder.binding.imageBrand.width,
                    holder.binding.imageBrand.height
                )
            )
            .into(holder.binding.imageBrand)

        holder.binding.brandName.text = getItem(position).title
    }

}

class DiffUtilsHomeBrand() : DiffUtil.ItemCallback<DisplayBrand>(){

    override fun areItemsTheSame(oldItem: DisplayBrand, newItem: DisplayBrand): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DisplayBrand, newItem: DisplayBrand): Boolean {
        return oldItem == newItem
    }

}