package com.dev.pizzahub.presentation.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.dev.pizzahub.R
import com.dev.pizzahub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

// This annotation is used to enable field injection in Android framework classes.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // AppBarConfiguration holds the top level destinations.
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Binding object instance with access to the views in the layout corresponding to the activity.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this activity.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create a NavController for this activity.
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Set up the AppBarConfiguration for the NavController.
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    // Handle Up button behavior.
    override fun onSupportNavigateUp(): Boolean {
        // Get the NavController for this activity.
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Delegate the Up button behavior to the NavController
        // If the Up button is not handled by the NavController, delegate to the parent activity.
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}