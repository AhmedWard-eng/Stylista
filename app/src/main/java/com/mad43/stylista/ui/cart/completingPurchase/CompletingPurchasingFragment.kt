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
import com.mad43.stylista.data.sharedPreferences.currency.CurrencyManager
import com.mad43.stylista.databinding.FragmentCompletingPurchasingBinding
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.remote.address.HasNoAddressException
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.setPrice
import kotlinx.coroutines.launch
import java.lang.Exception

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
                            viewModel.discountAmount = it.data.value
                            viewModel.discountType = it.data.value_type
                            setDiscountInText()
                            binding.textViewDiscountMessage.text = getString(R.string.discount_applied)
                        }
                        is RemoteStatus.Failure ->{
                            if (it.msg is CouponExpiredException){
                                binding.textViewDiscountMessage.visibility = VISIBLE
                                binding.textViewDiscountMessage.text = getString(R.string.coupon_is_expired)
                            }else if(it.msg is NotExistedException){
                                binding.textViewDiscountMessage.visibility = VISIBLE
                                binding.textViewDiscountMessage.text = getString(R.string.coupon_is_not_found)
                            }else if(it.msg is CantApplyDiscountException){
                                binding.textViewDiscountMessage.visibility = VISIBLE
                                binding.textViewDiscountMessage.text = getString(R.string.can_t_apply_that_coupon_to_that_pilling)
                            }
                        }
                        else ->{

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
                PaymentType.COD ->{

                }
                PaymentType.GOOGLE_PAY ->{
                    val googlePayRequest = GooglePayRequest()

                    googlePayRequest.transactionInfo = TransactionInfo.newBuilder()
                        .setTotalPrice("${viewModel.getOrderTotalPrice() - viewModel.discountAmount}")
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .setCurrencyCode(CurrencyManager().getCurrencyPair().first)
                        .build()
                    googlePayRequest.isBillingAddressRequired = true

                    googlePayClient.requestPayment(requireActivity(), googlePayRequest)
                }
            }
        }

        binding.buttonApplyCoupon.setOnClickListener {
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
        // complete Order
    }

    override fun onGooglePayFailure(error: Exception) {
        if (error is UserCanceledException) {
            Log.d("TAG", "userCancel: $error")
        } else {
            Log.d("TAG", "onGooglePayFailure: $error")
        }
    }
}