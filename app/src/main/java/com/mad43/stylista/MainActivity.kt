package com.mad43.stylista

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mad43.stylista.data.remote.network.CurrencyRetrofitService
import com.mad43.stylista.data.remote.network.currency.CurrencyApiInterface
import com.mad43.stylista.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.updateCurrencyRate()

        supportActionBar?.customView = binding.materialToolbar

        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

        setBottomBarVisibility()
        setActionBarVisibility()
//        setLabelInActionBar()

        setActionHint()
        binding.searchView.setOnClickListener {
            val currentDestination = navController.currentDestination?.id
            if (currentDestination == R.id.navigation_home) {
                navController.navigate(R.id.searchFragment)
            } else if (currentDestination != R.id.searchFragment) {
                navController.navigate(R.id.searchProductFragment)
            }
        }
        binding.imageFavorite.setOnClickListener {
            navController.navigate(R.id.favouriteFragment)
        }


        binding.imageCart.setOnClickListener {
            navController.navigate(R.id.cartFragment2)
        }
    }


    private fun setBottomBarVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_category, R.id.navigation_profile -> {
                    navView.visibility = View.VISIBLE
                }

                else -> {
                    navView.visibility = View.GONE
                }
            }
        }
    }

    private fun setActionBarVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.registrationFragment, R.id.logInFragment -> {
                    binding.materialToolbar.visibility = View.GONE
                }

                else -> {
                    binding.materialToolbar.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun setActionHint() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    binding.searchView.hint = getString(R.string.search_brand)
                    binding.searchView.isFocusable = false
                    binding.searchView.text.clear()
                }

                R.id.searchFragment -> {
                    binding.searchView.hint = getString(R.string.search_brand)
                    binding.searchView.isFocusableInTouchMode = true
                    binding.searchView.requestFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
                }

                R.id.searchProductFragment -> {
                    binding.searchView.hint = getString(R.string.search_product)
                    binding.searchView.isFocusableInTouchMode = true
                    binding.searchView.requestFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
                }

                else -> {
                    binding.searchView.isFocusable = false
                    binding.searchView.hint = getString(R.string.search_product)
                    binding.searchView.text.clear()
                }
            }
        }
    }


}