package com.mad43.stylista.ui.productInfo.view


import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.draftOrders.LineItem
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import com.mad43.stylista.data.remote.entity.draftOrders.Property
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPuttingRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.databinding.FragmentProductDetailsBinding
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModelFactory
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random


class ProductDetailsFragment : Fragment() , OnClickFavourite{

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var productInfo : ProductInfoViewModel
    lateinit var favFactory: ProductInfoViewModelFactory
    var availableSizesTitle = mutableListOf<String>()
    var availableSizesID = mutableListOf<Long>()
    lateinit var sizeIdPairs: List<Pair<String, Long>>
    var isFavourite : Boolean = false
    var selectedSize : String = ""
    var idVariansSelect: Long? = null
//    var lineItemsList = mutableListOf<InsertingLineItem>()
   // var customDraftOrderList= mutableListOf<CustomDraftOrderResponse>()
    //lateinit var requestBody : DraftOrderPuttingRequestBody
//    lateinit var favID : String
//    lateinit var lineItem1 :  InsertingLineItem

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

        val context = requireContext().applicationContext
        val localSource = ConcreteLocalSource(context)
        val favouriteLocalRepo = FavouriteLocalRepoImp(localSource)
        favFactory = ProductInfoViewModelFactory(ProductInfo(), FavouriteLocal(favouriteLocalRepo))
        productInfo = ViewModelProvider(this, favFactory).get(ProductInfoViewModel::class.java)

        if (id != null) {
            productInfo.getProductDetails(id)
            productInfo.isFavourite(id)
        }
        displayInfo()
        displayReviews()
        checkFavourite()
        addToCart(idVariansSelect)

    }

    private fun displayInfo(){
        lifecycleScope.launch {
            productInfo.favID = productInfo.getIDForFavourite().toString()
            // Get all favorites
            var  customDraftOrder = productInfo.getLineItems(productInfo.favID)
            productInfo.customDraftOrderList = customDraftOrder
            Log.d(TAG, "customDraftOrderListcustomDraftOrderListcustomDraftOrderList: ${productInfo.customDraftOrderList.size}, ${customDraftOrder.get(0).draft_order?.line_items?.get(0)?.title}")
            productInfo.uiState.collectLatest {
                    uiState ->when (uiState) {
                is ApiState.Success -> {
                    isDataSuccess()
                    val productTitle = uiState.data.product.title
                    val productPrice = uiState.data.product.variants.get(0).price
                    val productID = uiState.data.product.id
                    val productImage = uiState.data.product.images.get(0).src
                    val variantID = uiState.data.product.variants.get(0).id
                    binding.textViewProductName.text=productTitle
                    binding.textViewDescriptionScroll.text = uiState.data.product.body_html
                    binding.textViewDescriptionScroll.movementMethod = ScrollingMovementMethod()
                    binding.imageSlider.setImageList( productInfo.imagesArray, ScaleTypes.FIT)
                    binding.imageSlider.startSliding(2000)
                    binding.textViewPrice.text=productPrice
                    val randomFloat = Random.nextFloat() * 4.0f + 1.0f
                    binding.ratingBar.rating=randomFloat

                    uiState.data.product.variants.forEachIndexed { variantIndex, variant ->
                        Log.d(TAG, "Size: ${variant.title}, ${variant.id}")
                        binding.buttonAvailableSize.text = variant.title
                        val idVariants = variant.id
                        val size = variant.title
                        availableSizesTitle.add(size)
                        availableSizesID.add(idVariants)
                        sizeIdPairs = availableSizesTitle.zip(availableSizesID)
                    }
                    displayMenueAvaliableSize()

                    binding.imageViewFavourite.setOnClickListener {

                        var productFavourite = Favourite(id=productID, title = productTitle, price = productPrice, image = productImage, variantID =variantID )

                        val properties = listOf(
                            Property(name = "url_image", value = productImage)
                        )
                         productInfo.lineItem1 =  InsertingLineItem(
                            properties = properties,
                            variant_id = variantID,
                            quantity = 1,
                            price = productPrice,
                            title = productTitle
                        )

                        onClick(productFavourite)

                    }

                }
                is ApiState.Loading ->{
                    isDataLoading()
                }
                else -> {
                    Log.d(TAG, "onViewCreated: ${uiState}")
                }

            }
            }
        }
    }

    private fun addToCart(variantId: Long?) {
        binding.buttonAddToCart.setOnClickListener {
            if(variantId!= null){
                //idVarians
                val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment2(variantId)
                binding.root.findNavController().navigate(action)
            }else{
                MyDialog().showAlertDialog(getString(R.string.select_size),requireContext())
            }
        }
    }
    private fun displayMenueAvaliableSize(){
        val popupMenu = PopupMenu(requireContext(), binding.buttonAvailableSize)
        for (size in availableSizesTitle) {
            popupMenu.menu.add(size)
        }

        binding.buttonAvailableSize.setOnClickListener(View.OnClickListener { popupMenu.show() })

        popupMenu.setOnMenuItemClickListener { item ->
            selectedSize = item.title.toString()
            binding.buttonAvailableSize.text = selectedSize
            idVariansSelect = sizeIdPairs.find { it.first == selectedSize }?.second
            idVariansSelect?.let { addToCart(it)
                Log.d(TAG, "////////////idVariansSelect:::: ${idVariansSelect}")
            }
            true
        }
    }
    private fun isDataSuccess(){
        binding.progressBar.visibility=View.GONE
        binding.ratingBar.visibility=View.VISIBLE
        binding.buttonAvailableSize.visibility = View.VISIBLE
        binding.buttonAddToCart.visibility = View.VISIBLE
        binding.textViewReviews.visibility = View.VISIBLE
        binding.textViewDescriptionScroll.visibility = View.VISIBLE
    }
    private fun isDataLoading(){
        binding.progressBar.visibility=View.VISIBLE
        binding.ratingBar.visibility=View.GONE
        binding.buttonAvailableSize.visibility = View.GONE
        binding.buttonAddToCart.visibility = View.GONE
        binding.textViewReviews.visibility = View.GONE
        binding.textViewDescriptionScroll.visibility = View.GONE
    }
    override fun onClick(product: Favourite) {
        val message = if (isFavourite) {
            getString(R.string.is_remove_favourite)
        } else {
            getString(R.string.is_add_favourite)
        }
        MyDialog().showAlertDialog(message, requireContext()) {
            if (it) {
                if (isFavourite) {
                    productInfo.deleteProduct(product)
                    productInfo.insertAllProductToList()
                    Log.d(TAG, "displayInfo: ${productInfo.lineItemsList.size},, ${productInfo.lineItemsList.get(0).title}")

                    var newList = productInfo.lineItem1.variant_id?.let { it1 ->
                        productInfo.removeAnItemFromFavourite(productInfo.lineItemsList,
                            it1
                        )
                    }
                    Log.d(TAG, "newList: ${newList?.size},,${newList?.get(0)?.title}")
                    productInfo.requestBody = newList?.let { it1 ->
                        DraftOrderPuttingRequestBody(
                            line_items = it1
                        )
                    }!!
                    Log.d(TAG, "onClick: ${newList.size}")
                    productInfo.insertFavouriteForCustumer(productInfo.favID.toLong(), DraftOrderPutBody(productInfo.requestBody))
                    Toast.makeText(requireContext(), getString(R.string.remove_favourite), Toast.LENGTH_SHORT).show()
                } else {
                    productInfo.insertProduct(product)
                    productInfo.insertProductToList(productInfo.lineItem1)
                    Toast.makeText(requireContext(),  getString(R.string.add_favourite), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun checkFavourite(){
        lifecycleScope.launch {
            productInfo.isfavouriteExist.collectLatest {
                    uiState ->when (uiState) {
                is RemoteStatus.Success -> {
                    if(uiState.data){
                        binding.imageViewFavourite.setImageResource(R.drawable.baseline_favorite_24)
                        isFavourite = true
                    }else{
                        binding.imageViewFavourite.setImageResource(R.drawable.baseline_favorite_border_24)
                        isFavourite = false
                    }
                }
                else ->{

                }
            }
            }
        }
    }
    private fun displayReviews(){
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