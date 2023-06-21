package com.mad43.stylista.ui.productInfo.view


import android.app.AlertDialog
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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import com.mad43.stylista.data.remote.entity.draftOrders.Property
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.databinding.FragmentProductDetailsBinding
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.favourite.OnClickFavourite
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModelFactory
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random


class ProductDetailsFragment : Fragment() , OnClickFavourite {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    lateinit var productInfo : ProductInfoViewModel
    lateinit var favFactory: ProductInfoViewModelFactory
    var isFavourite : Boolean = false
    var isLogin : Boolean = false

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
        productInfo.checkUserIsLogin()
        observeLogin()
        checkFavourite()

        addToCart(productInfo.idVariansSelect,productInfo.selectedSize)


    }

    private fun displayInfo(){
        lifecycleScope.launch {


            productInfo.favID = productInfo.getIDForFavourite().toString()
            // Get all favorites
            var  customDraftOrder = productInfo.getLineItems(productInfo.favID)
            productInfo.customDraftOrderList = customDraftOrder
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
                        productInfo.availableSizesTitle.add(size)
                        productInfo.availableSizesID.add(idVariants)

                        productInfo.sizeIdPairs = productInfo.availableSizesTitle.zip(productInfo.availableSizesID)
                        Log.d(TAG, "displayInfo: ${productInfo.sizeIdPairs.size},,${productInfo.sizeIdPairs.get(0).first},,${productInfo.sizeIdPairs.get(0).second}")
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
if(isLogin){
    onClick(productFavourite)
}else{
      showConfirmationDialog()
    Log.d(TAG, "PPPPPPPlease LLLOgin :) ::::: ")
}


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

    private fun addToCart(variantId: Long?, nameItem : String) {
        binding.buttonAddToCart.setOnClickListener {
            if (isLogin){
                if(variantId!= null){
                    //idVarians
                    showConfirmationDialog(variantId,nameItem)
                }else{
                    MyDialog().showAlertDialog(getString(R.string.select_size),requireContext())
                }
            }else{
                showConfirmationDialog()
            }

        }
    }
    private fun displayMenueAvaliableSize(){
        val popupMenu = PopupMenu(requireContext(), binding.buttonAvailableSize)
        binding.buttonAvailableSize.text = getString(R.string.choose_size)
        binding.buttonAvailableSize.setOnClickListener(View.OnClickListener {
            popupMenu.menu.clear()
            for (size in productInfo.availableSizesTitle) {
                Log.d(TAG, "/////uniqueSizes: ${productInfo.availableSizesTitle.size}")
                popupMenu.menu.add(size)
            }
            popupMenu.show() }
        )

        popupMenu.setOnMenuItemClickListener { item ->
            productInfo.selectedSize = item.title.toString()
            binding.buttonAvailableSize.text = productInfo.selectedSize
            productInfo.idVariansSelect = productInfo.sizeIdPairs.find { it.first == productInfo.selectedSize }?.second
            productInfo.idVariansSelect?.let { addToCart(it,productInfo.selectedSize)
                Log.d(TAG, "////////////idVariansSelect:::: ${productInfo.idVariansSelect}")
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
                    productInfo.removeProductFromFavourite()
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

    private fun showConfirmationDialog(variantId: Long,nameItem: String) {
        val builder = AlertDialog.Builder(requireContext())
        var message = "${getString(R.string.added_to_cart_confirm)} ${nameItem} ${getString(R.string.addedd_countinue_cart)}"
        builder.setMessage(message)
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartFragment2(variantId)
                binding.root.findNavController().navigate(action)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        var message = "${getString(R.string.check_login)}"
        builder.setMessage(message)
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                Navigation.findNavController(requireView())
                    .navigate(R.id.logInFragment)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    fun observeLogin(){
        lifecycleScope.launch {
            productInfo.userExists.collect { userExists ->
                if (userExists) {
                    Log.d(TAG, "observeLogin: HHHHhhi login ${userExists}")
                    isLogin = true
                } else {
                    Log.d(TAG, "observeLogin: please, login:))))  ${userExists}")
                    isLogin = false
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}