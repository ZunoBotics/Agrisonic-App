package com.agrisonic.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrisonic.app.R
import com.agrisonic.app.data.local.PreferencesManager
import com.agrisonic.app.data.model.SignupRequest
import com.agrisonic.app.data.remote.RetrofitInstance
import com.agrisonic.app.databinding.ActivitySignupBinding
import com.agrisonic.app.ui.main.MainActivity
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSignup.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (validateInputs(name, email, password, confirmPassword)) {
                signup(name, email, password, confirmPassword)
            }
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty()) {
            showError("Please enter your name")
            return false
        }

        if (email.isEmpty()) {
            showError("Please enter your email")
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Please enter a valid email address")
            return false
        }

        if (password.isEmpty()) {
            showError("Please enter a password")
            return false
        }

        if (password.length < 8) {
            showError("Password must be at least 8 characters long")
            return false
        }

        if (password != confirmPassword) {
            showError("Passwords do not match")
            return false
        }

        return true
    }

    private fun signup(name: String, email: String, password: String, confirmPassword: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSignup.isEnabled = false
        binding.tvError.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val request = SignupRequest(
                    email = email,
                    name = name,
                    password = password,
                    confirmPassword = confirmPassword
                )

                val response = RetrofitInstance.authApi.signup(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data

                    // Show success message
                    Toast.makeText(
                        this@SignupActivity,
                        "Account created successfully! Please check your email for verification code.",
                        Toast.LENGTH_LONG
                    ).show()

                    // Navigate to verification screen
                    val intent = Intent(this@SignupActivity, VerifyCodeActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("from", "signup")
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = response.body()?.error ?: "Signup failed. Please try again."
                    showError(errorMessage)
                }
            } catch (e: Exception) {
                showError("Network error: ${e.message}")
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnSignup.isEnabled = true
            }
        }
    }

    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }
}
