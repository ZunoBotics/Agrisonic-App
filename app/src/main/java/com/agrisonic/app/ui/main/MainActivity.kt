package com.agrisonic.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.agrisonic.app.R
import com.agrisonic.app.databinding.ActivityMainBinding
import com.agrisonic.app.ui.fragments.DashboardHomeFragment
import com.agrisonic.app.ui.fragments.PlaceholderFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up bottom navigation
        setupBottomNavigation()

        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(DashboardHomeFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> {
                    loadFragment(PlaceholderFragment.newInstance("Explore"))
                    true
                }
                R.id.nav_favorites -> {
                    loadFragment(PlaceholderFragment.newInstance("Favorites"))
                    true
                }
                R.id.nav_home -> {
                    loadFragment(DashboardHomeFragment())
                    true
                }
                R.id.nav_notifications -> {
                    loadFragment(PlaceholderFragment.newInstance("Notifications"))
                    true
                }
                R.id.nav_settings -> {
                    loadFragment(PlaceholderFragment.newInstance("Settings"))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
