package com.mad43.stylista.ui.address.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.databinding.AddressItemBinding
import com.mad43.stylista.domain.model.AddressItem

class AddressAdapter(private val clickListener: ClickListener) :
    ListAdapter<AddressItem, AddressAdapter.ViewHolder>(DiffUtils) {

    class ViewHolder(val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding

        binding.buttonEdit.setOnClickListener {
            clickListener.edit(item)
        }
        binding.textViewAddressTitle.text = item.address1

        binding.textViewFullAddress.text = buildString {
            append(item.address2)
        }

        binding.textViewPhoneNumber.text = item.phone

        if (item.isDefault) {
            binding.checkBoxIsDefault.isChecked = true
            binding.checkBoxIsDefault.isEnabled = false
        } else {
            binding.checkBoxIsDefault.isEnabled = true
            binding.checkBoxIsDefault.isChecked = false
        }

        binding.checkBoxIsDefault.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.checkBoxIsDefault.isChecked = false
            clickListener.setDefault(item)
        }



        binding.root.setOnLongClickListener {
            clickListener.delete(item)
            true
        }
        binding.root.setOnClickListener {
            clickListener.select(item)
        }
    }


    object DiffUtils : DiffUtil.ItemCallback<AddressItem>() {
        override fun areItemsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
            return (oldItem.addressId == newItem.addressId)
        }

        override fun areContentsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
            return oldItem == newItem
        }
    }

    class ClickListener(
        private val editListener: (addressItem: AddressItem) -> Unit,
        private val deleteListener: (addressItem: AddressItem) -> Unit,
        private val setDefaultListener: (addressItem: AddressItem) -> Unit,
        private val selectListener: (addressItem: AddressItem) -> Unit
    ) {
        fun edit(addressItem: AddressItem) = editListener(addressItem)
        fun delete(addressItem: AddressItem) = deleteListener(addressItem)
        fun setDefault(addressItem: AddressItem) = setDefaultListener(addressItem)

        fun select(addressItem: AddressItem) = selectListener(addressItem)
    }


}


