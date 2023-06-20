package com.mad43.stylista.ui.productInfo.view


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.mad43.stylista.databinding.FragmentProductDetailsBinding
import com.mad43.stylista.domain.model.PuttingCartItem
import com.mad43.stylista.domain.remote.cart.PutItemInCartUseCase
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random


class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var productInfo : ProductInfoViewModel

    var availableSizes = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id= arguments?.getLong("id")

        productInfo = ViewModelProvider(this)[ProductInfoViewModel::class.java]
        binding.textViewReviews.setOnClickListener {
            val fragment = ReviewFragment()
            fragment.show(requireActivity().supportFragmentManager, "MyDialogReviewsFragment")
        }
        if (id != null) {
            productInfo.getProductDetails(id)
        }
        lifecycleScope.launch {
            productInfo.uiState.collectLatest {
                    uiState ->when (uiState) {
                is ApiState.Success -> {
                    binding.progressBar.visibility=View.GONE
                    binding.ratingBar.visibility=View.VISIBLE
                    binding.buttonAvailableSize.visibility = View.VISIBLE
                    binding.buttonAddToCart.visibility = View.VISIBLE
                    binding.textViewReviews.visibility = View.VISIBLE
                    binding.textViewDescriptionScroll.visibility = View.VISIBLE

                    binding.textViewProductName.text=uiState.data.product.title
                    binding.textViewDescriptionScroll.text = uiState.data.product.body_html
                    binding.textViewDescriptionScroll.movementMethod = ScrollingMovementMethod()
                    binding.imageSlider.setImageList( productInfo.imagesArray, ScaleTypes.FIT)
                    binding.imageSlider.startSliding(2000)
                    binding.textViewPrice.text=uiState.data.product.variants.get(0).price
                    val randomFloat = Random.nextFloat() * 4.0f + 1.0f
                    binding.ratingBar.rating=randomFloat
                    for (index in 0 .. uiState.data.product.variants.size-1){
                        Log.d(TAG, "Size: ${uiState.data.product.variants.get(index).title} ")
                        binding.buttonAvailableSize.text = uiState.data.product.variants.get(index).title
                        val size = uiState.data.product.variants.get(index).title
                        availableSizes.add(size)
                    }

                    val popupMenu = PopupMenu(requireContext(), binding.buttonAvailableSize)
                    for (size in availableSizes) {
                        popupMenu.menu.add(size)
                    }

                    binding.buttonAvailableSize.setOnClickListener(View.OnClickListener { popupMenu.show() })

                    popupMenu.setOnMenuItemClickListener { item ->
                        val selectedSize = item.title.toString()
                        binding.buttonAvailableSize.text = selectedSize
                        true
                    }
                }
                is ApiState.Loading ->{
                    binding.progressBar.visibility=View.VISIBLE
                    binding.ratingBar.visibility=View.GONE
                    binding.buttonAvailableSize.visibility = View.GONE
                    binding.buttonAddToCart.visibility = View.GONE
                    binding.textViewReviews.visibility = View.GONE
                    binding.textViewDescriptionScroll.visibility = View.GONE
                }
                else -> {
                    Log.d(TAG, "onViewCreated: ${uiState}")
                }


            }
            }
        }

        addToCart(0L)
    }

    private fun addToCart(variantId: Long) {
        binding.buttonAddToCart.setOnClickListener {
            variantId
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}