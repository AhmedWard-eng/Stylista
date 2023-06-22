package com.mad43.stylista.ui.cart.completingPurchase

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.braintreepayments.api.BraintreeClient
import com.braintreepayments.api.GooglePayClient
import com.braintreepayments.api.GooglePayListener
import com.braintreepayments.api.GooglePayRequest
import com.braintreepayments.api.PaymentMethodNonce
import com.braintreepayments.api.UserCanceledException
import com.google.android.gms.wallet.TransactionInfo
import com.google.android.gms.wallet.WalletConstants
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.entity.orders.DiscountCode
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.data.remote.entity.orders.post.order.CustomerOrder
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import com.mad43.stylista.data.sharedPreferences.currency.CurrencyManager
import com.mad43.stylista.databinding.FragmentCompletingPurchasingBinding
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.model.toOrderLineItems
import com.mad43.stylista.domain.remote.address.HasNoAddressException
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.setPrice
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import java.lang.Exception
import javax.annotation.meta.When

class CompletingPurchasingFragment : Fragment() ,GooglePayListener{

    private var _binding: FragmentCompletingPurchasingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var viewModel: CompletingPurchasingViewModel

    private lateinit var googlePayClient : GooglePayClient
    private lateinit var braintreeClient: BraintreeClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = CompletingPurchasingFragmentArgs.fromBundle(requireArguments())
        viewModel = ViewModelProvider(this)[CompletingPurchasingViewModel::class.java]
        viewModel.cartList = args.cartArray.toList()
        _binding = FragmentCompletingPurchasingBinding.inflate(inflater, container, false)
        braintreeClient =  BraintreeClient(requireContext().applicationContext, "sandbox_5r42nmbk_b7dn3gbb5qtxmw2w")
        googlePayClient = GooglePayClient(this,braintreeClient)
        googlePayClient.setListener(this)
        if (viewModel.goingToAddNewAddress){
            viewModel.getDefaultAddress()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.defaultAddressState.collect{
                    when(it){
                        is RemoteStatus.Success -> {
                            setAddressInView(it.data)
                            viewModel.address = it.data
                        }
                        is RemoteStatus.Failure ->{
                            if (it.msg is HasNoAddressException){
                                binding.groupAddress.visibility = GONE
                                binding.group.visibility = VISIBLE
                            }
                        }
                        else ->{

                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.validateCouponStatus.collect{
                    when(it){
                        is RemoteStatus.Success -> {
                            viewModel.discountCode = it.data.code
                            viewModel.discountAmount = it.data.value
                            viewModel.discountType = it.data.value_type
                            setDiscountInText()
                            binding.textViewDiscountMessage.text = getString(R.string.discount_applied)

                            binding.groupLoading.visibility = GONE
                        }
                        is RemoteStatus.Failure ->{

                            viewModel.discountCode = ""
                            viewModel.discountAmount = -0.0
                            viewModel.discountType = ""
                            setDiscountInText()

                            when (it.msg) {
                                is CouponExpiredException -> {
                                    binding.textViewDiscountMessage.visibility = VISIBLE
                                    binding.textViewDiscountMessage.text = getString(R.string.coupon_is_expired)

                                }

                                is NotExistedException -> {
                                    binding.textViewDiscountMessage.visibility = VISIBLE
                                    binding.textViewDiscountMessage.text = getString(R.string.coupon_is_not_found)
                                }

                                is CantApplyDiscountException -> {
                                    binding.textViewDiscountMessage.visibility = VISIBLE
                                    binding.textViewDiscountMessage.text = getString(R.string.can_t_apply_that_coupon_to_that_pilling)
                                }
                            }

                            binding.groupLoading.visibility = GONE
                        }
                        else ->{

                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.postingOrderState.collect{
                    when (it){
                        is RemoteStatus.Success -> {
                            binding.groupLoading.visibility = GONE
                            Navigation.findNavController(requireView()).navigate(R.id.action_completingPurchasingFragment_to_navigation_home)
                            viewModel.clearCart()
                            binding.groupLoading.visibility = GONE

                            MyDialog().showAlertDialog(getString(R.string.your_order_is_confirmed), requireContext())

                        }
                        is RemoteStatus.Failure ->{
                            binding.groupLoading.visibility = GONE
                            MyDialog().showAlertDialog(getString(R.string.something_went_wrong_while_completing_your_order),requireContext())
                        }
                        else -> {

                        }
                    }
                }
            }
        }

        binding.textViewDiscountPrice.setPrice(viewModel.discountAmount)

        binding.textViewSummaryPrice.setPrice(viewModel.getOrderTotalPrice() - viewModel.getDiscount())

        binding.radioGroup.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.radioButtonCOD -> viewModel.paymentType = PaymentType.COD
                R.id.radioButtonGP-> viewModel.paymentType = PaymentType.GOOGLE_PAY
            }
        }

        binding.buttonAddNewAddress.setOnClickListener {
            viewModel.goingToAddNewAddress = true
            val action = CompletingPurchasingFragmentDirections.actionCompletingPurchasingFragmentToAddressDetailsEditFragment(AddressItem(viewModel.userId!!,))
            Navigation.findNavController(requireView()).navigate(action)
        }
        binding.textViewOrderPrice.setPrice(viewModel.getOrderTotalPrice())

        binding.buttonPurchase.setOnClickListener {
            when(viewModel.paymentType){
                PaymentType.COD -> {
                    val discountCode = if(viewModel.discountCode.isNotBlank()){
                        listOf(DiscountCode(type = viewModel.discountType, amount = (viewModel.getDiscount() * -1).toString(), code = viewModel.discountCode))
                    }else{
                        null
                    }
                    viewModel.postOrder(PostOrderResponse(
                        order = Orders(
                            lineItems = viewModel.cartList.toOrderLineItems(),
                            discount_codes = discountCode,
                            email = viewModel.email,
                            customer = CustomerOrder(viewModel.userId)
                        )
                    ))
                }
                PaymentType.GOOGLE_PAY -> {
                    val googlePayRequest = GooglePayRequest()

                    googlePayRequest.transactionInfo = TransactionInfo.newBuilder()
                        .setTotalPrice("${viewModel.getOrderTotalPrice() - viewModel.discountAmount}")
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .setCurrencyCode(CurrencyManager().getCurrencyPair().first)
                        .build()
                    googlePayRequest.isBillingAddressRequired = true

                    googlePayClient.requestPayment(requireActivity(), googlePayRequest)

                    binding.groupLoading.visibility = VISIBLE
                }
            }
        }

        binding.buttonApplyCoupon.setOnClickListener {
            binding.groupLoading.visibility = VISIBLE
            viewModel.applyCoupon(binding.editTextcoupon.text.toString())
        }

    }

    private fun setDiscountInText() {
        binding.textViewDiscountPrice.setPrice(viewModel.getDiscount())
        binding.textViewSummaryPrice.setPrice(viewModel.getOrderTotalPrice() + viewModel.getDiscount())
    }

    private fun setAddressInView(addressItem: AddressItem) {
        binding.textViewAddressTitle.text = addressItem.address1

        binding.textViewFullAddress.text = buildString {
            append(addressItem.address2)
            append(", ")
            append(addressItem.city)
            append(", ")
            append(addressItem.province)
            append(", ")
            append(addressItem.country)
        }

        binding.textViewPhoneNumber.text = addressItem.phone
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGooglePaySuccess(paymentMethodNonce: PaymentMethodNonce) {
        val discountCode = if(viewModel.discountCode.isNotBlank()){
            listOf(DiscountCode(type = viewModel.discountType, amount = (viewModel.getDiscount() * -1).toString(), code = viewModel.discountCode))
        }else{
            null
        }
        viewModel.postOrder(PostOrderResponse(
            order = Orders(
                lineItems = viewModel.cartList.toOrderLineItems(),
                discount_codes = discountCode,
                email = viewModel.email,
                customer = CustomerOrder(viewModel.userId)
            )
        ))
        binding.groupLoading.visibility = VISIBLE
    }

    override fun onGooglePayFailure(error: Exception) {
        if (error is UserCanceledException) {
            Log.d("TAG", "userCancel: $error")
        } else {
            MyDialog().showAlertDialog(getString(R.string.try_again),requireContext())
            Log.d("TAG", "onGooglePayFailure: $error")
        }
        binding.groupLoading.visibility = GONE
    }
}