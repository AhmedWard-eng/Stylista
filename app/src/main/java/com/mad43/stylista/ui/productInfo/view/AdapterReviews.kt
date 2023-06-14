package com.mad43.stylista.ui.productInfo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.databinding.RowReviewsBinding
import com.mad43.stylista.domain.model.Review

class AdapterReviews (private var reviewList: List<Review>) :
    RecyclerView.Adapter<AdapterReviews.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ViewHolder(RowReviewsBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = reviewList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    fun setData(value: List<Review>){
        this.reviewList = value
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: RowReviewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.textViewTitle.text = review.title
            binding.textViewComment.text = review.comment
            binding.ratingBarReviews.rating = review.rating
        }
    }
}