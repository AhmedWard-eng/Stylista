package com.mad43.stylista.ui.cart

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mad43.stylista.databinding.FragmentCartBinding
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.setPrice
import kotlinx.coroutines.launch

class CartFragment : Fragment() {


    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var viewModel: CartViewModel

    lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CartAdapter(CartAdapter.ClickListener(::setQuantity, ::delete))
        binding.recyclerView.adapter = adapter

        handleScrollingBehavior()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productListStatus.collect{
                    when(it){
                        is RemoteStatus.Success ->{
                            binding.recyclerView.visibility = VISIBLE
                            binding.shimmerFrameLayout.visibility = GONE
                            binding.progressBar2.visibility = GONE
                            binding.blockingView.visibility = GONE
                            binding.shimmerFrameLayout.stopShimmerAnimation()
                            adapter.submitList(it.data)
                            viewModel.list = it.data
                            binding.textViewTotalPrice.setPrice(viewModel.getTotalPrice())
                            viewModel.deleteCommand.value = Action.Nothing
                            viewModel.editCommand.value = Action.Nothing
                        }
                        is RemoteStatus.Failure ->{
                            binding.recyclerView.visibility = VISIBLE
                            binding.shimmerFrameLayout.visibility = GONE
                            binding.progressBar2.visibility = GONE
                            binding.blockingView.visibility = GONE
                            binding.shimmerFrameLayout.stopShimmerAnimation()
                            viewModel.deleteCommand.value = Action.Nothing
                            viewModel.editCommand.value = Action.Nothing
                            Log.e("TAG", "onViewCreated: ",it.msg)
                        }
                        else -> {
                            binding.progressBar2.visibility = GONE
                            binding.recyclerView.visibility = GONE
                            binding.blockingView.visibility = GONE
                            binding.shimmerFrameLayout.startShimmerAnimation()
                        }
                    }
                }
            }
        }


    }

    private fun handleScrollingBehavior() {
        binding.recyclerView.addOnScrollListener(object : CartRecyclerScroll() {
            override fun show() {
                val params: ViewGroup.LayoutParams = binding.recyclerView.layoutParams
                params.height = 0
                binding.recyclerView.layoutParams = params
                binding.cardView.animate().translationY(0F)
                    .setInterpolator(DecelerateInterpolator(2f)).start()
            }

            override fun hide() {
                val params: ViewGroup.LayoutParams = binding.recyclerView.layoutParams
                params.height = LayoutParams.WRAP_CONTENT
                binding.recyclerView.layoutParams = params
                if ((binding.recyclerView.adapter?.itemCount ?: 0) > 5) {
                    binding.cardView.animate().translationY(binding.cardView.height.toFloat())
                        .setInterpolator(AccelerateInterpolator(2f)).start()
                } else {
                    params.height = 0
                    binding.recyclerView.layoutParams = params
                }
            }
        })
    }


    private fun setQuantity(variantId: Long, quantity: Int) {
        lifecycleScope.launch {
            viewModel.editCommand.emit(Action.Edit(variantId, quantity,adapter.currentList))
            binding.progressBar2.visibility = VISIBLE
            binding.blockingView.visibility = VISIBLE
        }
    }

    private fun delete(variantId: Long) {
        lifecycleScope.launch {
            viewModel.deleteCommand.emit(Action.Delete(variantId))
            binding.progressBar2.visibility = VISIBLE
            binding.blockingView.visibility = VISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}