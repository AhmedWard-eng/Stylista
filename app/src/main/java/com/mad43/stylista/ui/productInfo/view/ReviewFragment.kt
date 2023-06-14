package com.mad43.stylista.ui.productInfo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mad43.stylista.databinding.FragmentReviewsDialogBinding
import com.mad43.stylista.ui.productInfo.model.reviews


class ReviewFragment : DialogFragment() {


    private var _binding: FragmentReviewsDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReviewsDialogBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val adapter = AdapterReviews(reviews.take(3))

        binding.recyclerViewReviews.adapter = adapter
        binding.recyclerViewReviews.layoutManager = LinearLayoutManager(context)

        binding.textViewMoreThan.setOnClickListener {
            binding.textViewMoreThan.visibility = View.GONE
            adapter.setData(reviews)
        }
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}