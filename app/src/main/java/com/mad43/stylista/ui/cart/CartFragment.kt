package com.mad43.stylista.ui.cart

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mad43.stylista.databinding.FragmentCartBinding

class CartFragment : Fragment() {


    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var viewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = CartAdapter()

        binding.recyclerView.addOnScrollListener( object : CartRecyclerScroll() {
            override fun show() {

                val params: ViewGroup.LayoutParams = binding.recyclerView.layoutParams
                params.height = 0
                binding.recyclerView.layoutParams = params
                binding.cardView.animate().translationY(0F).setInterpolator(DecelerateInterpolator(2f)).start()
            }
            override fun hide() {
                val params: ViewGroup.LayoutParams = binding.recyclerView.layoutParams

                params.height = LayoutParams.WRAP_CONTENT
                binding.recyclerView.layoutParams = params
                if((binding.recyclerView.adapter?.itemCount ?: 0) > 5){
                    binding.cardView.animate().translationY(binding.cardView.height.toFloat()).setInterpolator( AccelerateInterpolator(2f)).start()
                }else{
                    params.height = 0
                    binding.recyclerView.layoutParams = params
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}