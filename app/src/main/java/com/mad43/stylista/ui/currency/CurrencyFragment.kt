package com.mad43.stylista.ui.currency

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import androidx.navigation.ui.NavigationUI
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.entity.currency.listOfCurrencies
import com.mad43.stylista.databinding.FragmentCartBinding
import com.mad43.stylista.databinding.FragmentCurrencyBinding
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.showConfirmationDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CurrencyFragment : Fragment() {

    private var _binding: FragmentCurrencyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var adapter: CurrencyAdapter

    private lateinit var viewModel: CurrencyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CurrencyViewModel::class.java]
        adapter = CurrencyAdapter(CurrencyAdapter.ClickListener(::onChangeCurrency))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.submitList(listOfCurrencies)
        binding.recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currencyRateState.collect { status ->
                    when (status) {
                        is RemoteStatus.Success -> {
                            binding.blockingView.visibility = GONE
                            binding.progressBar.visibility = GONE
                            showConfirmationDialog(
                                getString(
                                    R.string.changeCurrencyAssurance,
                                    status.data.second
                                )
                            ) {
                                val data = status.data
                                viewModel.setCurrencyInShared(Pair(data.second, data.first))
                                Navigation.findNavController(requireView()).navigateUp()
                            }
                        }

                        is RemoteStatus.Failure -> {
                            binding.blockingView.visibility = GONE
                            binding.progressBar.visibility = GONE
                            MyDialog().showAlertDialog("Something Went Wrong", requireContext())
                        }

                        is RemoteStatus.Loading -> {
                            binding.progressBar.visibility = VISIBLE
                            binding.blockingView.visibility = VISIBLE
                        }

                        else -> {

                        }
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onChangeCurrency(currencyCode: String) {
        viewModel.getCurrencyRate(currencyCode)
    }

}