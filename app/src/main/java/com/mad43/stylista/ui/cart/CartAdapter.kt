package com.mad43.stylista.ui.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.databinding.CartItemBinding

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = 5


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
}