package com.mad43.stylista.ui.home

import android.content.ClipData
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.text.ClipboardManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.mad43.stylista.databinding.FragmentHomeBinding
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.showDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnItemBrandClicked {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var brandAdapter: HomeBrandAdapter
    private lateinit var homeViewModel : HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
      
      homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getRandomCoupon()
        binding.imageSlider.setImageList(homeViewModel.ads, ScaleTypes.FIT)
        binding.imageSlider.startSliding(2000)
        binding.imageSlider.setItemClickListener(object  : ItemClickListener{
            override fun doubleClick(position: Int) {
            }

            override fun onItemSelected(position: Int) {
                if(homeViewModel.couponCode.isNotBlank()){
                    copyToClipBoard()
                }else{
                    Log.d("TAG", "onViewCreated: hello hello")
                }
            }

        })

        lifecycleScope.launch {
            homeViewModel.brands.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        binding.brandRecycle.visibility = View.GONE
                        binding.shimmerFrameLayout.startShimmerAnimation()
                    }

                    is RemoteStatus.Success -> {
                        binding.brandRecycle.visibility = View.VISIBLE
                        binding.shimmerFrameLayout.visibility = View.GONE
                        binding.shimmerFrameLayout.stopShimmerAnimation()
                        brandAdapter =
                            HomeBrandAdapter(requireContext(),this@HomeFragment)
                        binding.brandRecycle.apply {
                            adapter = brandAdapter
                            brandAdapter.submitList(it.data)
                            layoutManager = GridLayoutManager(context,2).apply {
                                orientation = RecyclerView.VERTICAL
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                homeViewModel.couponState.collectLatest {
                    when(it){
                        is RemoteStatus.Success ->{
                            homeViewModel.couponCode = it.data.code
                        }
                        is RemoteStatus.Loading ->{

                        }
                        else ->{
                            homeViewModel.getRandomCoupon()
                        }
                    }
                }
            }
        }
    }


    private fun copyToClipBoard() {
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = homeViewModel.couponCode
        } else {
            val clipboard =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("text", homeViewModel.couponCode)
            clipboard.setPrimaryClip(clip)
        }
        MyDialog().showAlertDialog("You got \"${homeViewModel.couponCode} \" coupon and is copied",requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun brandClicked(brand: String) {
        val action = HomeFragmentDirections.actionNavigationHomeToBrandFragment2(brand)
        binding.root.findNavController().navigate(action)
    }
}