package com.agrisonic.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrisonic.app.AgrisonicApplication
import com.agrisonic.app.databinding.ActivityLoginBinding
import com.agrisonic.app.data.model.LoginRequest
import com.agrisonic.app.data.remote.RetrofitInstance
import com.agrisonic.app.ui.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val preferencesManager by lazy {
        (application as AgrisonicApplication).preferencesManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                login(email, password)
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email format"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }

        return true
    }

    private fun login(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitInstance.authApi.login(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val loginData = response.body()?.data
                    val user = loginData?.user
                    val token = loginData?.token

                    if (user != null && token != null) {
                        // Save auth token (for Laravel Sanctum)
                        preferencesManager.saveSessionToken(token)

                        // Save user info
                        preferencesManager.saveUserInfo(
                            userId = user.id,
                            email = user.email,
                            name = user.name
                        )

                        // Navigate to main activity
                        Toast.makeText(
                            this@LoginActivity,
                            "Login successful!",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: Missing user or token data", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Laravel returns "message" field, not "error"
                    val errorMessage = response.body()?.error ?: response.errorBody()?.string() ?: "Login failed - Status: ${response.code()}"
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
            }
        }
    }
}
