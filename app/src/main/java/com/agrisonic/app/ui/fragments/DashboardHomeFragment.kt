package com.agrisonic.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.agrisonic.app.AgrisonicApplication
import com.agrisonic.app.R
import com.agrisonic.app.databinding.FragmentDashboardHomeBinding
import com.agrisonic.app.ui.adapters.FeatureItem
import com.agrisonic.app.ui.adapters.FeaturesAdapter
import com.agrisonic.app.ui.auth.LoginActivity
import kotlinx.coroutines.launch

class DashboardHomeFragment : Fragment() {

    private var _binding: FragmentDashboardHomeBinding? = null
    private val binding get() = _binding!!

    private val preferencesManager by lazy {
        (requireActivity().application as AgrisonicApplication).preferencesManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupClickListeners()
        setupFeaturesGrid()
        loadWeatherData()
    }

    private fun setupUI() {
        // Display user name
        viewLifecycleOwner.lifecycleScope.launch {
            preferencesManager.getUserName().collect { userName ->
                binding.tvUserName.text = userName ?: "Guest"
            }
        }

        // Set current date/time
        updateDateTime()
    }

    private fun setupClickListeners() {
        binding.btnMenu.setOnClickListener {
            // TODO: Open navigation drawer
            Toast.makeText(requireContext(), "Menu - Coming soon", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            handleLogout()
        }

        binding.btnRefreshWeather.setOnClickListener {
            loadWeatherData()
        }
    }

    private fun handleLogout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Clear user data
                viewLifecycleOwner.lifecycleScope.launch {
                    preferencesManager.clearAllData()

                    // Navigate to login
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateDateTime() {
        val currentTime = java.text.SimpleDateFormat("EEE, MMM d, yyyy h:mm:ss a", java.util.Locale.getDefault())
            .format(java.util.Date())
        binding.tvDateTime.text = currentTime
    }

    private fun setupFeaturesGrid() {
        val features = listOf(
            FeatureItem("Crop Prediction", android.R.drawable.ic_menu_sort_by_size, "/crop-prediction"),
            FeatureItem("Nutrient Recommendation", android.R.drawable.ic_dialog_info, "/nutrient-recommendation"),
            FeatureItem("Nearby Buyers", android.R.drawable.ic_menu_mylocation, "/nearby-buyers"),
            FeatureItem("Crop Information", android.R.drawable.ic_menu_info_details, "/crop-information"),
            FeatureItem("Farm Assistant", android.R.drawable.ic_menu_help, "/farm-assistant"),
            FeatureItem("Pest Control", android.R.drawable.ic_menu_report_image, "/pest-control"),
            FeatureItem("Advanced Features", android.R.drawable.ic_menu_manage, "/advanced-features")
        )

        val adapter = FeaturesAdapter(features) { feature ->
            Toast.makeText(requireContext(), "Coming soon: ${feature.title}", Toast.LENGTH_SHORT).show()
        }

        binding.rvFeatures.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFeatures.adapter = adapter
    }

    private fun loadWeatherData() {
        // TODO: Fetch weather data from API
        binding.tvLocation.text = "Kampala, Uganda"
        binding.tvTemperature.text = "28°C"
        binding.tvHumidity.text = "65%"
        binding.tvWindSpeed.text = "12 km/h"
        binding.tvPressure.text = "1013 hPa"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
