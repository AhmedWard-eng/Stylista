package com.mad43.stylista.ui.productInfo.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentProductInfoBinding
import com.mad43.stylista.databinding.FragmentRegistrationBinding
import com.mad43.stylista.ui.home.HomeViewModel
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel

class ProductInfoFragment : Fragment() {

    private var _binding: FragmentProductInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductInfoBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productInfoViewModel =
            ViewModelProvider(this)[ProductInfoViewModel::class.java]

        binding.imageSlider.setImageList(productInfoViewModel.testArray, ScaleTypes.FIT)
        binding.imageSlider.startSliding(3000)

        binding.textViewDescriptionScroll.movementMethod = ScrollingMovementMethod()

        binding.textViewReviews.setOnClickListener {
            val fragment = ReviewFragment()
            fragment.show(requireFragmentManager(), "MyDialogReviewsFragment")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}