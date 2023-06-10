package com.mad43.stylista

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
        setLabelInActionBar()

        binding.searchView.setOnClickListener {
            navController.navigate(R.id.searchFragment)
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

    @SuppressLint("SetTextI18n")
    private fun setLabelInActionBar(){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    binding.fragmentName.text = "Home"
                }
                R.id.navigation_dashboard -> {
                    binding.fragmentName.text = "Category"
                }
                R.id.navigation_notifications -> {
                    binding.fragmentName.text = "Profile"
                }
                R.id.brandFragment -> {
                    binding.fragmentName.text = "Brand’s Products"
                }
                R.id.productDetailsFragment -> {
                    binding.fragmentName.text = "Product’s Details"
                }
                R.id.searchFragment -> {
                    binding.fragmentName.text = "Search"
                }
                else -> {
                }
            }
        }

    }
}