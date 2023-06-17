package com.mad43.stylista.ui.currency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.data.remote.entity.currency.currencies
import com.mad43.stylista.databinding.CurrencyItemBinding

class CurrencyAdapter(private val clickListener: ClickListener) : ListAdapter<String,CurrencyAdapter.ViewHolder>(DiffUtils) {

    class ViewHolder(val binding: CurrencyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.textViewCurrencyName.text = currencies[item]
        holder.binding.textViewCurrencyCode.text = item
        holder.binding.root.setOnClickListener {
            clickListener.onClick(currencyCode = item)
        }
    }


    object DiffUtils : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    class ClickListener(private val clickListener : (String) -> Unit){
        fun onClick(currencyCode : String) = clickListener(currencyCode)
    }
}


