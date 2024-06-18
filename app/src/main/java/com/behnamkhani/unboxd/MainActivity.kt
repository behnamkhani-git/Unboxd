package com.behnamkhani.unboxd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.behnamkhani.unboxd.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy { findNavController(com.behnamkhani.unboxd.R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareUI()
    }

    private fun prepareUI() {
        prepareBottomNav()
        prepareToolbar()
    }

    private fun prepareToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.hide()
    }

    private fun prepareBottomNav() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}