package com.mad43.stylista.ui.productInfo.view

import android.content.ContentValues
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.mad43.stylista.databinding.FragmentProductDetailsBinding
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random


class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var productInfo : ProductInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id= arguments?.getLong("id")

        productInfo = ViewModelProvider(this)[ProductInfoViewModel::class.java]
        binding.textViewReviews.setOnClickListener {
            val fragment = ReviewFragment()
            fragment.show(requireFragmentManager(), "MyDialogReviewsFragment")
        }

        println("/*****////****")
        if (id != null) {
            productInfo.getProductDetails(id)
        }
        lifecycleScope.launch {
            productInfo.uiState.collectLatest {
                    uiState ->when (uiState) {
                is ApiState.Success -> {
                    binding.textViewProductName.text=uiState.data.product.title.toString()
                    binding.textViewDescriptionScroll.text = uiState.data.product.body_html.toString()
                    binding.textViewDescriptionScroll.movementMethod = ScrollingMovementMethod()
                    binding.imageSlider.setImageList( productInfo.imagesArray, ScaleTypes.FIT)
                    binding.imageSlider.startSliding(2000)
                    binding.textViewPrice.text=uiState.data.product.variants.get(0).price
                    val randomFloat = Random.nextFloat() * 4.0f + 1.0f
                    binding.ratingBar.rating=randomFloat
                    Log.d(ContentValues.TAG, "onViewCreated: ${uiState.data.product.title}")
                }
                else -> {
                    Log.d(ContentValues.TAG, "onViewCreated: ${uiState}")
                }
            }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}