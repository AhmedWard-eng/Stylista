package com.mad43.stylista

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mad43.stylista.databinding.ActivityMainBinding
import com.mad43.stylista.ui.home.HomeFragmentDirections
import com.mad43.stylista.ui.search.view.SearchFragmentDirections
import com.mad43.stylista.util.Constants.Companion.TITE_FRAGMENT_BRAND
import com.mad43.stylista.util.Constants.Companion.TITE_FRAGMENT_PRODUCT
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    private var titeFragment: String = ""
    val bundle = Bundle()
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
        //  setActionBarFragment()

        binding.searchView.setOnClickListener {
            Log.d(TAG, "/////////////////////// navigation: ")
            navController.addOnDestinationChangedListener { _, destination, _ ->
                titeFragment = if (destination.id == R.id.navigation_home) {
                    TITE_FRAGMENT_BRAND
                } else {
                    TITE_FRAGMENT_PRODUCT
                }

            }
            navController.navigate(R.id.searchProductFragment)

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

//    private fun setActionBarFragment() {
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.navigation_home -> {
//                    titeFragment = "Brand"
//
//                }
//                else -> {
//                    titeFragment = "Product"
//                }
//            }
////            val action = SearchFragmentDirections.actionSearchFragmentToBrandFragment(titeFragment)
////            binding.root.findNavController().navigate(action)
//        }
//    }

}