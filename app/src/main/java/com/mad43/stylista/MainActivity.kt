package com.mad43.stylista

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mad43.stylista.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            } else {
                navController.navigate(R.id.searchProductFragment)
            }
        }

    }


    private fun setBottomBarVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications -> {
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

//
//    @SuppressLint("SetTextI18n")
//    private fun setLabelInActionBar(){
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.navigation_home -> {
//                    binding.fragmentName.text = resources.getString(R.string.title_home)
//                }
//                R.id.navigation_dashboard -> {
//                    binding.fragmentName.text = resources.getString(R.string.category)
//                }
//                R.id.navigation_notifications -> {
//                    binding.fragmentName.text = resources.getString(R.string.profile)
//                }
//                R.id.brandFragment -> {
//                    binding.fragmentName.text = resources.getString(R.string.brand_Products)
//                }
//                R.id.productDetailsFragment3 -> {
//                    binding.fragmentName.text = resources.getString(R.string.product_Details)
//                }
//                R.id.searchFragment -> {
//                    binding.fragmentName.text = resources.getString(R.string.search)
//                }
//                else -> {
//                }
//            }
//        }
//
//    }
    private fun setActionHint() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home-> {
                    binding.searchView.hint = getString(R.string.search_brand)
                }
                else -> {
                    binding.searchView.hint = getString(R.string.search_product)
                }
            }
        }
    }


}